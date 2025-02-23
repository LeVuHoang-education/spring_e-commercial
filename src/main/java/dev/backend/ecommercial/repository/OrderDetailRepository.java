package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.OrderDetails;
import dev.backend.ecommercial.model.entity.Orders;
import dev.backend.ecommercial.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {
    Optional<OrderDetails> findByOrderAndProduct(Orders order, Product product);
    Optional<OrderDetails> findByOrderDetailId(Long orderDetailId);
    List<OrderDetails> findByOrder(Orders order);

}
