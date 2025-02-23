package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long categoryId);
    Optional<Category> findByCategoryName(String categoryName);
}
