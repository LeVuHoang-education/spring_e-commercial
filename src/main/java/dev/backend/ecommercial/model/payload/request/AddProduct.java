package dev.backend.ecommercial.model.payload.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AddProduct {
    private String productName;
    private BigDecimal productPrice;
    private String productDescription;
    private String productImage;
    private int productQuantity;
    private Long categoryId;
}
