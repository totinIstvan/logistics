package hu.webuni.totinistvan.logistics.service;

import hu.webuni.totinistvan.logistics.model.entity.Address;
import hu.webuni.totinistvan.logistics.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void deleteById(long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
        }
    }
}
