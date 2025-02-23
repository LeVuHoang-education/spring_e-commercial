package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.Cart;
import dev.backend.ecommercial.model.entity.Product;
import dev.backend.ecommercial.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart>findByUser(User user);
    Cart findByUserAndProduct(User user, Product product);
    List<Cart> findAllByUser(User user);
    Boolean existsCartByUser(User user);
    void deleteAllByUser(User user);
}
