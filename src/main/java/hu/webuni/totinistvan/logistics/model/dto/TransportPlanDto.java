package hu.webuni.totinistvan.logistics.model.dto;

import java.util.List;

public class TransportPlanDto {

    private Long id;
    private int revenue;
    private List<SectionDto> sections;

    public TransportPlanDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public List<SectionDto> getSections() {
        return sections;
    }

    public void setSections(List<SectionDto> sections) {
        this.sections = sections;
    }
}
