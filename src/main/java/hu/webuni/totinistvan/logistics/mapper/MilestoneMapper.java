package hu.webuni.totinistvan.logistics.mapper;

import hu.webuni.totinistvan.logistics.model.dto.TransportPlanDto;
import hu.webuni.totinistvan.logistics.model.entity.Milestone;
import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;

import java.util.List;

public interface MilestoneMapper {

    List<TransportPlanDto> transportPlansToDtos(List<TransportPlan> transportPlans);

    Milestone dtoToTransportPlan(TransportPlanDto transportPlanDto);

    TransportPlanDto transportPlanToDto(TransportPlan transportPlan);
}
