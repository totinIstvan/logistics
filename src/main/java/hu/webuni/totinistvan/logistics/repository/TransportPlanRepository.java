package hu.webuni.totinistvan.logistics.repository;

import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportPlanRepository extends JpaRepository<TransportPlan, Long> {
}
