package dev.backend.ecommercial.model.dto;

import dev.backend.ecommercial.model.entity.Orders;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {
    private Long orderId;
    private Long userId;
    private String userName;
    private String orderStatus;
    private String paymentMethod;
    private double orderTotal;

    public static OrderDto toDto(Orders order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .userName(order.getUser().getUserName())
                .orderStatus(String.valueOf(order.getOrderStatus()))
                .paymentMethod(order.getPaymentMethod())
                .orderTotal(order.getOrderTotal().doubleValue())
                .build();
    }

    public static OrderDto from(Orders savedOrder) {
        return null;
    }
}
