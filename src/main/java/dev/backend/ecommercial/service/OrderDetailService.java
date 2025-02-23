package dev.backend.ecommercial.service;

import dev.backend.ecommercial.model.dto.OrderDetailDto;
import dev.backend.ecommercial.model.entity.OrderDetails;
import dev.backend.ecommercial.model.entity.Orders;
import dev.backend.ecommercial.model.entity.Product;
import dev.backend.ecommercial.model.entity.User;
import dev.backend.ecommercial.model.payload.request.AddOrderDetails;
import dev.backend.ecommercial.model.payload.response.ResponseData;
import dev.backend.ecommercial.model.payload.response.ResponseError;
import dev.backend.ecommercial.repository.OrderDetailRepository;
import dev.backend.ecommercial.repository.OrderRepository;
import dev.backend.ecommercial.repository.ProductRepository;
import dev.backend.ecommercial.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository ordersRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository, OrderRepository ordersRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.ordersRepository = ordersRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public ResponseData<OrderDetailDto> createOrderDetail(AddOrderDetails form, Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Orders order = ordersRepository.findById(orderId).orElse(null);
        if (order == null) {
            return new ResponseError<>(400, "Order not found");
        }
        User currentUser = userRepository.findByEmail(email).orElse(null);
        if (currentUser == null) {
            return new ResponseError<>(400, "User not found");
        }
        Product product = productRepository.findById(form.getProductId()).orElse(null);
        if (product == null) {
            return new ResponseError<>(400, "Product not found");
        }

        OrderDetails oD = new OrderDetails();
        oD.setOrder(order);
        oD.setProduct(product);
        oD.setQuantity(form.getQuantity());
        oD.setPrice(product.getProductPrice().multiply(BigDecimal.valueOf(form.getQuantity())));

        OrderDetails saveOD = orderDetailRepository.save(oD);
        return new ResponseData<>(200, "Order detail created successfully", OrderDetailDto.from(saveOD));
    }

    public ResponseData<List<OrderDetailDto>> getAllOrderDetails(Long orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        try {
            List<OrderDetailDto> listOrderDetails = orderDetailRepository.findByOrder(order).stream()
                    .map(OrderDetailDto::from)
                    .collect(Collectors.toList());
            return new ResponseData<>(200, "Retrieved all order details successfully", listOrderDetails);
        } catch (Exception e) {
            return new ResponseError<>(500, "Failed to retrieve order details: " + e.getMessage());
        }

    }
}
