package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.Orders;
import dev.backend.ecommercial.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByOrderId(Long orderId);

    List<Orders> findByUserId(User userId);

    List<Orders> findByStatus(Orders.Status status);

}
