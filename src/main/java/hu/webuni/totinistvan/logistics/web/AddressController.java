package hu.webuni.totinistvan.logistics.web;

import hu.webuni.totinistvan.logistics.mapper.AddressMapper;
import hu.webuni.totinistvan.logistics.model.dto.AddressDto;
import hu.webuni.totinistvan.logistics.model.dto.AddressFilterDto;
import hu.webuni.totinistvan.logistics.model.entity.Address;
import hu.webuni.totinistvan.logistics.service.AddressService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    public AddressController(AddressService addressService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

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
    public AddressDto addNew(@RequestBody @Valid AddressDto addressDto) {
        if (addressDto.getId() == null) {
            return addressMapper.addressToDto(addressService.save(addressMapper.dtoToAddress(addressDto)));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable long id, @RequestBody @Valid AddressDto addressDto) {
        if (addressDto.getId() == null || addressDto.getId() == id) {
            Address address = addressMapper.dtoToAddress(addressDto);
            address.setId(id);
            try {
                AddressDto savedAddress = addressMapper.addressToDto(addressService.update(address));
                return ResponseEntity.ok(savedAddress);
            } catch (NoSuchElementException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            addressService.deleteById(id);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address with id " + id +
                    " cannot be deleted from the database because it still associated with an existing transport plan");
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<AddressDto>> getByExample(@RequestBody AddressFilterDto example,
                                                         @PageableDefault(size = 100)
                                                         @SortDefault.SortDefaults({
                                                                 @SortDefault(sort = "id"),
                                                                 @SortDefault(direction = Sort.Direction.ASC)
                                                         }) Pageable pageable) {
        Page<Address> page = addressService.findAddressesByExample(example, pageable);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return new ResponseEntity<>(addressMapper.addressesToDtos(page.getContent()), httpHeaders, HttpStatus.OK);
    }
}
