package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.Category;
import dev.backend.ecommercial.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long productId);
    Optional<Product> findByProductName(String productName);
    Optional<Product> findByProductPrice(BigDecimal productPrice);
    List<Product> findByCategory(Category category);

}
