package hu.webuni.totinistvan.logistics.service;

import hu.webuni.totinistvan.logistics.model.entity.Milestone;
import hu.webuni.totinistvan.logistics.model.entity.Section;
import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;
import hu.webuni.totinistvan.logistics.repository.MilestoneRepository;
import hu.webuni.totinistvan.logistics.repository.TransportPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TransportPlanService {

    private final TransportPlanRepository transportPlanRepository;

    private final MilestoneRepository milestoneRepository;

    private final RevenueService revenueService;

    public TransportPlanService(TransportPlanRepository transportPlanRepository,
                                MilestoneRepository milestoneRepository,
                                RevenueService revenueService) {
        this.transportPlanRepository = transportPlanRepository;
        this.milestoneRepository = milestoneRepository;
        this.revenueService = revenueService;
    }

    public List<TransportPlan> findAll() {
        return transportPlanRepository.findAll();
    }

    public Optional<TransportPlan> findById(long id) {
        return transportPlanRepository.findById(id);
    }

    @Transactional
    public TransportPlan save(TransportPlan transportPlan) {
        return transportPlanRepository.save(transportPlan);
    }

    @Transactional
    public TransportPlan update(TransportPlan transportPlan) {
        if (transportPlanRepository.existsById(transportPlan.getId())) {
            return save(transportPlan);
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public void deleteById(long id) {
        if (transportPlanRepository.existsById(id)) {
            transportPlanRepository.deleteById(id);
        }
    }

    @Transactional
    public void addDelayToMilestones(long id, long milestoneId, long delayInMinutes) {
        if (transportPlanRepository.existsById(id) && milestoneRepository.existsById(milestoneId)) {
            TransportPlan transportPlan = transportPlanRepository.findById(id).get();
            List<Section> sections = transportPlan.getSections();

            boolean hasTransportPlanMilestone = hasTransportPlanMilestone(milestoneId, sections);

            if (hasTransportPlanMilestone) {
                setDelay(milestoneId, delayInMinutes, sections);
                int reducedRevenue = revenueService.reduceRevenue(transportPlan, delayInMinutes);
                transportPlan.setRevenue(reducedRevenue);
            } else {
                throw new InvalidParameterException();
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    private boolean hasTransportPlanMilestone(long milestoneId, List<Section> sections) {
        return sections.stream()
                .anyMatch(section -> section.getFromMilestone().getId() == milestoneId || section.getToMilestone().getId() == milestoneId);
    }

    private void setDelay(long milestoneId, long delayInMinutes, List<Section> sections) {
        Milestone fromMilestone;
        Milestone toMilestone;
        int numberOfSections = sections.size();

        for (Section section : sections) {
            fromMilestone = section.getFromMilestone();
            toMilestone = section.getToMilestone();

            if (fromMilestone.getId() == milestoneId) {
                fromMilestone.setPlannedTime(fromMilestone.getPlannedTime().plusMinutes(delayInMinutes));
                toMilestone.setPlannedTime(toMilestone.getPlannedTime().plusMinutes(delayInMinutes));
                return;
            } else if (toMilestone.getId() == milestoneId) {
                toMilestone.setPlannedTime(toMilestone.getPlannedTime().plusMinutes(delayInMinutes));

                if (!isLastSection(section, numberOfSections)) {
                    fromMilestone = sections.get(section.getNumber() + 1).getFromMilestone();

                    if (isOnSameDay(fromMilestone, toMilestone)) {
                        fromMilestone.setPlannedTime(fromMilestone.getPlannedTime().plusMinutes(delayInMinutes));
                    }
                }
                return;
            }
        }
    }

    private boolean isLastSection(Section section, int numberOfSections) {
        return section.getNumber() == numberOfSections - 1;
    }

    private boolean isOnSameDay(Milestone fromMilestone, Milestone toMilestone) {
        return fromMilestone.getPlannedTime().getDayOfMonth() == toMilestone.getPlannedTime().getDayOfMonth();
    }
}
