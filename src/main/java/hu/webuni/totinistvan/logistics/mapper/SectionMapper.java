package hu.webuni.totinistvan.logistics.mapper;

import hu.webuni.totinistvan.logistics.model.dto.SectionDto;
import hu.webuni.totinistvan.logistics.model.entity.Section;

import java.util.List;

public interface SectionMapper {

    List<SectionDto> sectionsToDtos(List<Section> sections);

    Section dtoToSection(SectionDto sectionDto);

    SectionDto sectionToDto(Section section);
}
