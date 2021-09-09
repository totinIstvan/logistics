package hu.webuni.totinistvan.logistics.service;

import hu.webuni.totinistvan.logistics.config.RevenueConfigProperties;
import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
public class RevenueService {

    RevenueConfigProperties revenueConfigProperties;

    public RevenueService(RevenueConfigProperties revenueConfigProperties) {
        this.revenueConfigProperties = revenueConfigProperties;
    }

    public int reduceRevenue(TransportPlan transportPlan, Long delay) {
        int revenue = transportPlan.getRevenue();

        TreeMap<Long, Double> reductionPercents = revenueConfigProperties.getReductionPercents();

        double actualPercent = reductionPercents.floorEntry(delay).getValue();

        return (int) (revenue - (revenue * (actualPercent / 100)));
    }
}
