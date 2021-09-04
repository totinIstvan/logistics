package hu.webuni.totinistvan.logistics.service;

import hu.webuni.totinistvan.logistics.model.entity.Address;
import hu.webuni.totinistvan.logistics.model.entity.Address_;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecifications {

    public static Specification<Address> hasCountryCode(String countryCode) {
        return (root, cq, cb) -> cb.equal(root.get(Address_.countryCode), countryCode);
    }

    public static Specification<Address> hasZipCode(String zipCode) {
        return (root, cq, cb) -> cb.equal(root.get(Address_.zipCode), zipCode);
    }

    public static Specification<Address> hasCity(String city) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get(Address_.city)), (city + "%").toLowerCase());
    }

    public static Specification<Address> hasStreet(String street) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get(Address_.street)), (street + "%").toLowerCase());
    }
}
