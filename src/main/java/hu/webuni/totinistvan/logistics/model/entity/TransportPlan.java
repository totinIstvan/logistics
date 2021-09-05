package hu.webuni.totinistvan.logistics.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class TransportPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int revenue;

    @OneToMany(mappedBy = "transportPlan")
    private List<Section> sections;

    public TransportPlan() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
