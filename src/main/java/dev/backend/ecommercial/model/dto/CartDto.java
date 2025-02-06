package dev.backend.ecommercial.model.dto;

import dev.backend.ecommercial.model.entity.Cart;
import lombok.*;

import java.security.PrivateKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private Long cartId;
    private Long userId;
    private Long productId;
    private int quantity;

    public static CartDto from(Cart cart) {
        return CartDto.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .productId(cart.getProduct().getProductId())
                .quantity(cart.getQuantity())
                .build();
    }
}
