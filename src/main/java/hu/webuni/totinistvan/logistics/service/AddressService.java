package hu.webuni.totinistvan.logistics.service;

import hu.webuni.totinistvan.logistics.model.dto.AddressFilterDto;
import hu.webuni.totinistvan.logistics.model.entity.Address;
import hu.webuni.totinistvan.logistics.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Optional<Address> findById(long id) {
        return addressRepository.findById(id);
    }

    @Transactional
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    public Address update(Address address) {
        if (addressRepository.existsById(address.getId())) {
            return save(address);
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public void deleteById(long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
        }
    }

    public Page<Address> findAddressesByExample(AddressFilterDto example, Pageable pageable) {
        String countryCode = example.getCountryCode();
        String zipCode = example.getZipCode();
        String city = example.getCity();
        String street = example.getStreet();

        Specification<Address> spec = Specification.where(null);

        if (StringUtils.hasText(countryCode)) {
            spec = spec.and(AddressSpecifications.hasCountryCode(countryCode));
        }

        if (StringUtils.hasText(zipCode)) {
            spec = spec.and(AddressSpecifications.hasZipCode(zipCode));
        }

        if (StringUtils.hasText(city)) {
            spec = spec.and(AddressSpecifications.hasCity(city));
        }

        if (StringUtils.hasText(street)) {
            spec = spec.and(AddressSpecifications.hasStreet(street));
        }

        return addressRepository.findAll(spec, pageable);
    }
}
