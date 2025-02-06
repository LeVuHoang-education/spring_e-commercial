package dev.backend.ecommercial.model.payload.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddOrderDetails {
    private Long orderId;
    private Long productId;
    private int quantity;
    private BigDecimal price;
}
