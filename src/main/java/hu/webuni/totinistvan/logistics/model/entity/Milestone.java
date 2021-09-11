package hu.webuni.totinistvan.logistics.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Address address;

    private LocalDateTime plannedTime;

    public Milestone() {
    }

    public Milestone(Address address, LocalDateTime plannedTime) {
        this.address = address;
        this.plannedTime = plannedTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDateTime getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(LocalDateTime plannedTime) {
        this.plannedTime = plannedTime;
    }
}
