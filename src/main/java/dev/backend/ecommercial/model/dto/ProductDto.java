package dev.backend.ecommercial.model.dto;

import dev.backend.ecommercial.model.entity.Product;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productDescription;
    private String productImage;
    private int productQuantity;
    private Long categoryId;
    private Boolean active;

    public static ProductDto from (Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productDescription(product.getProductDescription())
                .productImage(product.getProductImage())
                .productQuantity(product.getProductQuantity())
                .categoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null)
                .active(product.getActive())
                .build();
    }
}
