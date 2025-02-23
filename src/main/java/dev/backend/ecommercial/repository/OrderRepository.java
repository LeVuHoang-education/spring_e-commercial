package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.Orders;
import dev.backend.ecommercial.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findById(Long orderId);

    List<Orders> findByUser(User user);

    List<Orders> findByOrderStatus(Orders.Status orderStatus);

    List<Orders> findByUser_UserId(Long userId);
}
