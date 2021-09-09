package hu.webuni.totinistvan.logistics.model.dto;

public class DelayDto {

    private long milestoneId;
    private int minutesOfDelay;

    public DelayDto() {
    }

    public long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public int getMinutesOfDelay() {
        return minutesOfDelay;
    }

    public void setMinutesOfDelay(int minutesOfDelay) {
        this.minutesOfDelay = minutesOfDelay;
    }
}
