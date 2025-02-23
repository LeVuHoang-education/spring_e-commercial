package dev.backend.ecommercial.controller.user;


import dev.backend.ecommercial.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("**")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/user/products")
public class ProductController {
    private final ProductService productService;


    @GetMapping("/get-all-products")
    public ResponseEntity getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/get-product-by-category/{categoryId}")
    public ResponseEntity getProductByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getByCategory(categoryId));
    }
}
