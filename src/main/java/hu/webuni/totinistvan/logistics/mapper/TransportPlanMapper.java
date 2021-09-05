package hu.webuni.totinistvan.logistics.mapper;

import hu.webuni.totinistvan.logistics.model.dto.SectionDto;
import hu.webuni.totinistvan.logistics.model.dto.TransportPlanDto;
import hu.webuni.totinistvan.logistics.model.entity.Section;
import hu.webuni.totinistvan.logistics.model.entity.TransportPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransportPlanMapper {

    List<TransportPlanDto> transportPlansToDtos(List<TransportPlan> transportPlans);

    TransportPlan dtoToTransportPlan(TransportPlanDto transportPlanDto);

    TransportPlanDto transportPlanToDto(TransportPlan transportPlan);

    @Mapping(target = "transportPlan", ignore = true)
    SectionDto sectionToDto(Section section);
}
