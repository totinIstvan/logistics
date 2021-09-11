package hu.webuni.totinistvan.logistics.model.entity;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private TransportPlan transportPlan;

    @OneToOne
    private Milestone fromMilestone;

    @OneToOne
    private Milestone toMilestone;

    private int number;

    public Section() {
    }

    public Section(TransportPlan transportPlan, Milestone fromMilestone, Milestone toMilestone, int number) {
        this.transportPlan = transportPlan;
        this.fromMilestone = fromMilestone;
        this.toMilestone = toMilestone;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Milestone getFromMilestone() {
        return fromMilestone;
    }

    public void setFromMilestone(Milestone fromMilestone) {
        this.fromMilestone = fromMilestone;
    }

    public Milestone getToMilestone() {
        return toMilestone;
    }

    public void setToMilestone(Milestone toMilestone) {
        this.toMilestone = toMilestone;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public TransportPlan getTransportPlan() {
        return transportPlan;
    }

    public void setTransportPlan(TransportPlan transportPlan) {
        this.transportPlan = transportPlan;
    }
}
