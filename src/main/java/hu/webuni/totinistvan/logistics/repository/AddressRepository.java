package hu.webuni.totinistvan.logistics.repository;

import hu.webuni.totinistvan.logistics.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
