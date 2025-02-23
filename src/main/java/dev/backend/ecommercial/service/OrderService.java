package dev.backend.ecommercial.service;

import dev.backend.ecommercial.model.dto.OrderDto;
import dev.backend.ecommercial.model.entity.Cart;
import dev.backend.ecommercial.model.entity.OrderDetails;
import dev.backend.ecommercial.model.entity.Orders;
import dev.backend.ecommercial.model.entity.User;
import dev.backend.ecommercial.model.payload.request.AddOrders;
import dev.backend.ecommercial.model.payload.response.ResponseData;
import dev.backend.ecommercial.repository.CartRepository;
import dev.backend.ecommercial.repository.OrderDetailRepository;
import dev.backend.ecommercial.repository.OrderRepository;
import dev.backend.ecommercial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartRepository cartRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Transactional
    public ResponseData<OrderDto> createOrder(AddOrders orderForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email).orElse(null);
        if (currentUser == null) {
            return new ResponseData<>(400, "User not found with email: " + email, null);
        }

        List<Cart> cartItems = cartRepository.findByUser(currentUser);
        if (cartItems.isEmpty()) {
            return new ResponseData<>(400, "Cart is empty", null);
        }

        Orders order = new Orders();
        order.setUser(currentUser);
        order.setOrderStatus(Orders.Status.PENDING);
        order.setPaymentMethod(orderForm.getPaymentMethod());
        order.setOrderTotal(cartItems.stream()
                .map(c -> c.getProduct().getProductPrice().multiply(BigDecimal.valueOf(c.getQuantity())) )
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        Orders savedOrder = orderRepository.save(order);

        List<OrderDetails> orderDetailsList = cartItems.stream()
                .map(cartItem -> {
                    OrderDetails orderDetail = new OrderDetails();
                    orderDetail.setOrder(savedOrder);
                    orderDetail.setProduct(cartItem.getProduct());
                    orderDetail.setQuantity(cartItem.getQuantity());
                    orderDetail.setPrice(cartItem.getProduct().getProductPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    return orderDetail;
                })
                .collect(Collectors.toList());

        orderDetailRepository.saveAll(orderDetailsList);

        cartRepository.deleteAllByUser(currentUser);
        return new ResponseData<>(200, "Order created successfully", OrderDto.toDto(savedOrder));
    }

    public ResponseData<List<OrderDto>> getAll() {
        List<OrderDto> listOrders = orderRepository.findAll().stream()
                .map(OrderDto::toDto)
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Retrieved all orders successfully", listOrders);
    }

    public ResponseData<List<OrderDto>> getOrderById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<OrderDto> orders = orderRepository.findByUser(user).stream()
                .map(OrderDto::toDto)
                .collect(Collectors.toList());

        if (orders.isEmpty()) {
            return new ResponseData<>(200, "No orders found for the given user", Collections.emptyList());
        }

        return new ResponseData<>(200, "Search results retrieved successfully", orders);
    }



}
