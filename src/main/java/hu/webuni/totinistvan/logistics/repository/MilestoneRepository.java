package hu.webuni.totinistvan.logistics.repository;

import hu.webuni.totinistvan.logistics.model.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
}
