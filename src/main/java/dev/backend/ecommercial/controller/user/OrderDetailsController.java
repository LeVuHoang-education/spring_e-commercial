package dev.backend.ecommercial.controller.user;


import dev.backend.ecommercial.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin("**")
@Validated
@RestController
@RequestMapping("/api/v1/user/order-details")
public class OrderDetailsController {
    private final OrderDetailService orderDetailsService;

    @GetMapping("/get-all-order-details/{orderId}")
    public ResponseEntity getAllOrderDetails(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderDetailsService.getAllOrderDetails(orderId));
    }
}
