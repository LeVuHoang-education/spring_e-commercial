package dev.backend.ecommercial.model.payload.request;

import lombok.Data;

@Data
public class AddCart {
    private Long userId;
    private Long productId;
    private int quantity;
}
