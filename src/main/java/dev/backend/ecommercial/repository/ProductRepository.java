package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long productId);
    Optional<Product> findByName(String name);
    Optional<Product> findByPrice(Double price);
    List<Product> findByCategory(String category);

}
