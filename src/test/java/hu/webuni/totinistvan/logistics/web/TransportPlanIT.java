package hu.webuni.totinistvan.logistics.web;

import hu.webuni.totinistvan.logistics.model.dto.DelayDto;
import hu.webuni.totinistvan.logistics.model.dto.LoginDto;
import hu.webuni.totinistvan.logistics.model.dto.SectionDto;
import hu.webuni.totinistvan.logistics.model.dto.TransportPlanDto;
import hu.webuni.totinistvan.logistics.model.entity.Address;
import hu.webuni.totinistvan.logistics.model.entity.Milestone;
import hu.webuni.totinistvan.logistics.model.entity.Section;
import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;
import hu.webuni.totinistvan.logistics.repository.AddressRepository;
import hu.webuni.totinistvan.logistics.repository.MilestoneRepository;
import hu.webuni.totinistvan.logistics.repository.SectionRepository;
import hu.webuni.totinistvan.logistics.repository.TransportPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureWebTestClient(timeout = "36000")
public class TransportPlanIT {

    private static final String BASE_URI = "/api/transportPlans";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TransportPlanRepository transportPlanRepository;

    private String jwtToken;
    private TransportPlan testTransportPlan;
    private final List<Milestone> milestones = new ArrayList<>();

    @BeforeEach
    void init() {
        this.testTransportPlan = new TransportPlan(1_000_000);
        Address address = new Address("CC", "Test City", "Test street", "Test ZIP", "123", 11.111111, 11.111111);

        List<Section> sections = new ArrayList<>();

        long duration = 0L;
        for (int i = 0; i < 6; i++) {
            if (i < 4) {
                this.milestones.add(new Milestone(address, LocalDateTime.of(2021, 9, 10, 6, 0, 0).plusHours(duration)));
            } else {
                this.milestones.add(new Milestone(address, LocalDateTime.of(2021, 9, 11, 0, 0, 0).plusHours(duration)));
            }
            duration += 2;
        }

        int milestoneIndex = 0;
        for (int i = 0; i < 3; i++) {
            sections.add(new Section(testTransportPlan, milestones.get(milestoneIndex++), milestones.get(milestoneIndex++), i));
        }

        this.testTransportPlan = transportPlanRepository.save(testTransportPlan);
        addressRepository.save(address);
        milestoneRepository.saveAll(milestones);
        sectionRepository.saveAll(sections);
        testTransportPlan.setSections(sections);
        if (jwtToken == null) {
            login();
        }
    }

    private void login() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("TransportUser");
        loginDto.setPassword("pass");

        this.jwtToken = webTestClient.post()
                .uri("/api/login")
                .bodyValue(loginDto)
                .exchange()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void addDelayToMilestones_addedThirtyMinutesDelayToFromMilestone_returnsSameMilestoneAndReducesRevenueByTwoPercents() {
        DelayDto delayDto = new DelayDto();
        long milestoneId = this.testTransportPlan.getSections().get(0).getFromMilestone().getId();
        delayDto.setMilestoneId(milestoneId);
        delayDto.setMinutesOfDelay(30);

        long transportPlanId = this.testTransportPlan.getId();

        TransportPlanDto beforeTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();

        registerDelayToMilestone(delayDto, transportPlanId)
                .expectStatus().isOk();

        TransportPlanDto resultTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();

        int reducedRevenue = reduceRevenue(beforeTp.getRevenue(), 2);

        assertThat(beforeTp.getId()).isEqualTo(resultTp.getId());
        assertThat(beforeTp.getSections())
                .usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("fromMilestone", "toMilestone")
                .containsExactlyElementsOf(resultTp.getSections());
        assertThat(resultTp.getRevenue()).isEqualTo(reducedRevenue);
    }

    @Test
    void addDelayToMilestones_addedThirtyMinutesDelayToFromMilestone_increasesPlannedTimeOnlyOfTheActualSection() {
        DelayDto delayDto = new DelayDto();
        long milestoneId = this.testTransportPlan.getSections().get(0).getFromMilestone().getId();
        delayDto.setMilestoneId(milestoneId);
        delayDto.setMinutesOfDelay(30);

        long transportPlanId = this.testTransportPlan.getId();

        TransportPlanDto beforeTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();
        List<SectionDto> beforeSections = beforeTp.getSections();

        registerDelayToMilestone(delayDto, transportPlanId)
                .expectStatus().isOk();

        TransportPlanDto resultTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();
        List<SectionDto> afterSections = resultTp.getSections();

        assertThat(afterSections.get(0).getFromMilestone().getPlannedTime())
                .isEqualTo(beforeSections.get(0).getFromMilestone().getPlannedTime().plusMinutes(30));
        assertThat(afterSections.get(0).getToMilestone().getPlannedTime())
                .isEqualTo(beforeSections.get(0).getToMilestone().getPlannedTime().plusMinutes(30));
        assertThat(afterSections.get(1).getFromMilestone().getPlannedTime())
                .isEqualTo(beforeSections.get(1).getFromMilestone().getPlannedTime());
    }

    @Test
    void addDelayToMilestones_addedOneHourDelayToToMilestone_returnsReducedRevenueByThreeAndAHalfPercents() {
        DelayDto delayDto = new DelayDto();
        long milestoneId = this.testTransportPlan.getSections().get(0).getToMilestone().getId();
        delayDto.setMilestoneId(milestoneId);
        delayDto.setMinutesOfDelay(60);

        long transportPlanId = this.testTransportPlan.getId();

        TransportPlanDto beforeTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();

        registerDelayToMilestone(delayDto, transportPlanId)
                .expectStatus().isOk();

        TransportPlanDto resultTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();

        int reducedRevenue = reduceRevenue(beforeTp.getRevenue(), 3.5);

        assertThat(resultTp.getRevenue()).isEqualTo(reducedRevenue);
    }

    @Test
    void addDelayToMilestones_addedOneHourDelayToToMilestone_increasesPlannedTimeOnlyAtTheToMilestoneAndNextFromMilestone() {
        DelayDto delayDto = new DelayDto();
        long milestoneId = this.testTransportPlan.getSections().get(0).getToMilestone().getId();
        delayDto.setMilestoneId(milestoneId);
        delayDto.setMinutesOfDelay(60);

        long transportPlanId = this.testTransportPlan.getId();

        TransportPlanDto beforeTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();
        List<SectionDto> beforeSections = beforeTp.getSections();

        registerDelayToMilestone(delayDto, transportPlanId)
                .expectStatus().isOk();

        TransportPlanDto resultTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();
        List<SectionDto> afterSections = resultTp.getSections();

        assertThat(afterSections.get(0).getToMilestone().getPlannedTime())
                .isEqualTo(beforeSections.get(0).getToMilestone().getPlannedTime().plusMinutes(60));
        assertThat(afterSections.get(1).getFromMilestone().getPlannedTime())
                .isEqualTo(beforeSections.get(1).getFromMilestone().getPlannedTime().plusMinutes(60));
        assertThat(afterSections.get(1).getToMilestone().getPlannedTime())
                .isEqualTo(beforeSections.get(1).getToMilestone().getPlannedTime());
    }

    @Test
    void addDelayToMilestones_addedTwoHoursDelayToToMilestone_returnsReducedRevenueByEightPercents() {
        DelayDto delayDto = new DelayDto();
        long milestoneId = this.testTransportPlan.getSections().get(1).getToMilestone().getId();
        delayDto.setMilestoneId(milestoneId);
        delayDto.setMinutesOfDelay(120);

        long transportPlanId = this.testTransportPlan.getId();

        TransportPlanDto beforeTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();

        registerDelayToMilestone(delayDto, transportPlanId)
                .expectStatus().isOk();

        TransportPlanDto resultTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();

        int reducedRevenue = reduceRevenue(beforeTp.getRevenue(), 8);

        assertThat(resultTp.getRevenue()).isEqualTo(reducedRevenue);
    }

    @Test
    void addDelayToMilestones_addedTwoHoursDelayToToMilestone_doesNotIncreasesPlannedTimeAtTheNextMilestoneIfItIsOnAnotherDay() {
        DelayDto delayDto = new DelayDto();
        long milestoneId = this.testTransportPlan.getSections().get(1).getToMilestone().getId();
        delayDto.setMilestoneId(milestoneId);
        delayDto.setMinutesOfDelay(120);

        long transportPlanId = this.testTransportPlan.getId();

        TransportPlanDto beforeTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();
        List<SectionDto> beforeSections = beforeTp.getSections();

        registerDelayToMilestone(delayDto, transportPlanId)
                .expectStatus().isOk();

        TransportPlanDto resultTp = getTransportPlanById(transportPlanId)
                .expectBody(TransportPlanDto.class)
                .returnResult()
                .getResponseBody();
        List<SectionDto> afterSections = resultTp.getSections();

        assertThat(afterSections.get(1).getToMilestone().getPlannedTime())
                .isEqualTo(beforeSections.get(1).getToMilestone().getPlannedTime().plusMinutes(120));
        assertThat(afterSections.get(2).getFromMilestone().getPlannedTime())
                .isEqualTo(beforeSections.get(2).getFromMilestone().getPlannedTime());
    }

    @Test
    void addDelayToMilestones_callWithInvalidTransportPlanId_shouldReturnHttpStatusCode404NotFound() {
        DelayDto delayDto = new DelayDto();
        long milestoneId = this.testTransportPlan.getSections().get(1).getToMilestone().getId();
        delayDto.setMilestoneId(milestoneId);
        delayDto.setMinutesOfDelay(120);

        long invalidTransportPlanId = transportPlanRepository.findAll().size() + 1;

        registerDelayToMilestone(delayDto, invalidTransportPlanId)
                .expectStatus()
                .is4xxClientError()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void addDelayToMilestones_callWithInvalidMilestoneId_shouldReturnHttpStatusCode404NotFound() {
        DelayDto delayDto = new DelayDto();
        long milestoneId = milestoneRepository.findAll().size() + 1;
        delayDto.setMilestoneId(milestoneId);
        delayDto.setMinutesOfDelay(120);

        long transportPlanId = this.testTransportPlan.getId();

        registerDelayToMilestone(delayDto, transportPlanId)
                .expectStatus()
                .is4xxClientError()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void addDelayToMilestones_callWithMilestoneWitchNotBelongsToTransportPlan_shouldReturnHttpStatusCode400BadRequest() {
        Address address = new Address("CC", "Test City", "Test street", "Test ZIP", "123", 11.111111, 11.111111);
        Milestone extraMilestone = new Milestone(address, LocalDateTime.of(2021, 9, 10, 6, 0, 0));
        addressRepository.save(address);
        long extraMilestoneId = milestoneRepository.save(extraMilestone).getId();

        DelayDto delayDto = new DelayDto();
        delayDto.setMilestoneId(extraMilestoneId);
        delayDto.setMinutesOfDelay(120);

        long transportPlanId = this.testTransportPlan.getId();

        registerDelayToMilestone(delayDto, transportPlanId)
                .expectStatus()
                .is4xxClientError()
                .expectStatus()
                .isBadRequest();
    }

    private ResponseSpec registerDelayToMilestone(DelayDto delay, long id) {
        return webTestClient.post()
                .uri(BASE_URI + "/" + id + "/delay")
                .headers(headers -> headers.setBearerAuth(jwtToken))
                .bodyValue(delay)
                .exchange();
    }

    private ResponseSpec getTransportPlanById(long id) {
        return webTestClient.get()
                .uri(BASE_URI + "/" + id)
                .exchange();
    }

    private int reduceRevenue(int revenue, double percent) {
        return (int) (revenue * ((100 - percent) / 100));
    }
}
