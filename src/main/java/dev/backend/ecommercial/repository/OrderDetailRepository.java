package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {
    Optional<OrderDetails> findByOrderIdAndProductId(Long orderId, Long productId);
    Optional<OrderDetails> findByOrderDetailId(Long orderDetailId);
    List<OrderDetails> findByOrderId(Long orderId);

}
