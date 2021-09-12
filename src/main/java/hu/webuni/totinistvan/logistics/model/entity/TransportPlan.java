package hu.webuni.totinistvan.logistics.model.entity;

import javax.persistence.*;
import java.util.List;

@NamedEntityGraph(
        name = "TransportPlan.full",
        attributeNodes = @NamedAttributeNode(value = "sections", subgraph = "transportPlan.sections"),
        subgraphs = {
                @NamedSubgraph(name = "transportPlan.sections",
                        attributeNodes = {
                                @NamedAttributeNode(value = "fromMilestone", subgraph = "fromMilestone.address"),
                                @NamedAttributeNode(value = "toMilestone", subgraph = "toMilestone.address")}),
                @NamedSubgraph(name = "fromMilestone.address",
                        attributeNodes = @NamedAttributeNode(value = "address")),
                @NamedSubgraph(name = "toMilestone.address",
                        attributeNodes = @NamedAttributeNode(value = "address"))
        }
)
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

    public TransportPlan(int revenue) {
        this.revenue = revenue;
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
