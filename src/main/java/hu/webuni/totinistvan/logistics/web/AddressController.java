package hu.webuni.totinistvan.logistics.web;

import hu.webuni.totinistvan.logistics.mapper.AddressMapper;
import hu.webuni.totinistvan.logistics.model.dto.AddressDto;
import hu.webuni.totinistvan.logistics.model.entity.Address;
import hu.webuni.totinistvan.logistics.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressMapper addressMapper;

    @GetMapping
    public List<AddressDto> getAll() {
        return addressMapper.addressesToDtos(addressService.findAll());
    }

    @GetMapping("/{id}")
    public AddressDto getById(@PathVariable long id) {
        Address address = addressService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return addressMapper.addressToDto(address);
    }

    @PostMapping
    public AddressDto addNew(@RequestBody AddressDto addressDto) {
        return addressMapper.addressToDto(addressService.save(addressMapper.dtoToAddress(addressDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable long id, @RequestBody AddressDto addressDto) {
        Address address = addressMapper.dtoToAddress(addressDto);
        address.setId(id);
        try {
            AddressDto savedAddress = addressMapper.addressToDto(addressService.update(address));
            return ResponseEntity.ok(savedAddress);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            addressService.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}