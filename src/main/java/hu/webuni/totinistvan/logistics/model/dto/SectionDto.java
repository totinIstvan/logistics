package hu.webuni.totinistvan.logistics.model.dto;

public class SectionDto {

    private Long id;
    private TransportPlanDto transportPlan;
    private MilestoneDto fromMilestone;
    private MilestoneDto toMilestone;
    private int number;

    public SectionDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportPlanDto getTransportPlan() {
        return transportPlan;
    }

    public void setTransportPlan(TransportPlanDto transportPlan) {
        this.transportPlan = transportPlan;
    }

    public MilestoneDto getFromMilestone() {
        return fromMilestone;
    }

    public void setFromMilestone(MilestoneDto fromMilestone) {
        this.fromMilestone = fromMilestone;
    }

    public MilestoneDto getToMilestone() {
        return toMilestone;
    }

    public void setToMilestone(MilestoneDto toMilestone) {
        this.toMilestone = toMilestone;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
