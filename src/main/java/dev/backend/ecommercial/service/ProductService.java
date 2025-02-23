package dev.backend.ecommercial.service;


import dev.backend.ecommercial.model.dto.CategoryDto;
import dev.backend.ecommercial.model.dto.ProductDto;
import dev.backend.ecommercial.model.entity.Category;
import dev.backend.ecommercial.model.payload.response.ResponseData;
import dev.backend.ecommercial.repository.CategoryRepository;
import dev.backend.ecommercial.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;



@Log4j2
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;

        this.categoryRepository = categoryRepository;
    }

    public ResponseData<List<ProductDto>> getAll() {
        try {
            List<ProductDto> listProducts = productRepository.findAll().stream()
                    .map(ProductDto::from)
                    .collect(Collectors.toList());
            return new ResponseData<>(200, "Retrieved all products successfully", listProducts);
        } catch (Exception e) {
            return new ResponseData<>(500, "Failed to retrieve products: " + e.getMessage(), Collections.emptyList());
        }
    }

    public ResponseData<ProductDto> getById(Long id) {
        try {
            ProductDto productDto = ProductDto.from(productRepository.findById(id)
                    .orElseThrow(() -> new Exception("Product not found")));
            return new ResponseData<>(200, "Retrieved product successfully", productDto);
        } catch (Exception e) {
            return new ResponseData<>(500, "Failed to retrieve product: " + e.getMessage(), null);
        }
    }

    public ResponseData<List<ProductDto>> getByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        try {
            List<ProductDto> listProducts = productRepository.findByCategory(category).stream()
                    .map(ProductDto::from)
                    .collect(Collectors.toList());
            return new ResponseData<>(200, "Retrieved products by category successfully", listProducts);
        } catch (Exception e) {
            return new ResponseData<>(500, "Failed to retrieve products by category: " + e.getMessage(), Collections.emptyList());
        }
    }


    public ResponseData<String> delete(Long id) {
        try {
            if (!productRepository.existsById(id)) {
                return new ResponseData<>(404, "Product not found", "");
            }
            productRepository.deleteById(id);
            return new ResponseData<>(200, "Deleted product successfully", "");
        } catch (EmptyResultDataAccessException e) {
            return new ResponseData<>(404, "Product not found", "");
        } catch (Exception e) {
            return new ResponseData<>(500, "Failed to delete product: " + e.getMessage(), "");
        }
    }

    public ResponseData<String> disableProduct(Long id) throws Exception {
        try {
            ProductDto productDto = ProductDto.from(productRepository.findById(id)
                    .orElseThrow(() -> new Exception("Product not found")));
            productDto.setActive(false);
            return  new ResponseData<>(200, "Disabled product successfully", "disabled successfully");
        }catch (Exception e){
            return new ResponseData<>(500, "Failed to disable product: " + e.getMessage(), "");
        }
    }

}
