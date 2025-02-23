package dev.backend.ecommercial.controller.user;


import dev.backend.ecommercial.model.payload.request.AddOrders;
import dev.backend.ecommercial.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("**")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/user/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/get-all-orders/{userId}")
    public ResponseEntity getAllOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrderById(userId));
    }

    @PostMapping("/new-order")
    public ResponseEntity createOrder(@RequestBody AddOrders orderForm) {
        return ResponseEntity.ok(orderService.createOrder(orderForm));
    }
}
