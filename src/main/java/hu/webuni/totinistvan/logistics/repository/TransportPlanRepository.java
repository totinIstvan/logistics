package hu.webuni.totinistvan.logistics.repository;

import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransportPlanRepository extends JpaRepository<TransportPlan, Long> {

    @EntityGraph("TransportPlan.full")
    @Query("SELECT tp FROM TransportPlan tp")
    List<TransportPlan> findAllWithSections();

    @EntityGraph("TransportPlan.full")
    @Query("SELECT tp FROM TransportPlan tp WHERE tp.id = :id")
    Optional<TransportPlan> findByIdWithSections(long id);
}
