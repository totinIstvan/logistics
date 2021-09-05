package hu.webuni.totinistvan.logistics.model.dto;

import java.time.LocalDateTime;

public class MilestoneDto {

    private Long id;
    private AddressDto address;
    private LocalDateTime plannedTime;

    public MilestoneDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public LocalDateTime getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(LocalDateTime plannedTime) {
        this.plannedTime = plannedTime;
    }
}
