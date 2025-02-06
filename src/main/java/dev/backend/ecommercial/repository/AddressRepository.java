package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findById(Long addressId);
    List<Address> findByUserId(Long userId);
}
