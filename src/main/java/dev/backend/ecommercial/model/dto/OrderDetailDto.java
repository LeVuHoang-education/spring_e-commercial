package dev.backend.ecommercial.model.dto;

import dev.backend.ecommercial.model.entity.OrderDetails;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
public class OrderDetailDto {
    private Long orderDetailId;
    private Long orderId;
    private Long productId;
    private int quantity;
    private BigDecimal price;

    public static OrderDetailDto from (OrderDetails orderDetails) {
        return OrderDetailDto.builder()
                .orderDetailId(orderDetails.getOrderDetailId())
                .orderId(orderDetails.getOrder().getOrderId())
                .productId(orderDetails.getProduct().getProductId())
                .quantity(orderDetails.getQuantity())
                .price(orderDetails.getPrice())
                .build();
    }
}
