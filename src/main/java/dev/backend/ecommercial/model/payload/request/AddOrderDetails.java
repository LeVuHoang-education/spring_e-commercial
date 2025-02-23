    package dev.backend.ecommercial.model.payload.request;

    import dev.backend.ecommercial.model.entity.Product;
    import lombok.Data;
    import java.math.BigDecimal;

    @Data
    public class AddOrderDetails {
        private Long productId;
        private int quantity;
        private BigDecimal price;
    }
