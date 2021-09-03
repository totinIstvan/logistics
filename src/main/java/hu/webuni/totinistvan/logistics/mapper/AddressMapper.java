package hu.webuni.totinistvan.logistics.mapper;

import hu.webuni.totinistvan.logistics.model.entity.Address;
import hu.webuni.totinistvan.logistics.model.dto.AddressDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    List<AddressDto> addressesToDtos(List<Address> addresses);

    AddressDto addressToDto(Address address);

    Address dtoToAddress(AddressDto addressDto);
}
