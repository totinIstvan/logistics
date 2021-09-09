package hu.webuni.totinistvan.logistics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

@ConfigurationProperties(prefix = "revenue")
@Component
public class RevenueConfigProperties {

    private TreeMap<Long, Double> reductionPercents;

    public TreeMap<Long, Double> getReductionPercents() {
        return reductionPercents;
    }

    public void setReductionPercents(TreeMap<Long, Double> reductionPercents) {
        this.reductionPercents = reductionPercents;
    }
}
