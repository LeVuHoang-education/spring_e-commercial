package dev.backend.ecommercial.model.payload.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddOrders {
    private Long userId;
    private String userName;
    private String orderStatus;
    private String paymentMethod;
    private BigDecimal orderTotal;
}
