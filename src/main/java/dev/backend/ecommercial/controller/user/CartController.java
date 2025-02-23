package dev.backend.ecommercial.controller.user;

import dev.backend.ecommercial.model.payload.request.AddCart;
import dev.backend.ecommercial.service.CartService;
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
@RequestMapping("api/v1/user/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/get-cart/{userId}")
    public ResponseEntity getCart( @PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getAllProductForUser(userId));
    }

    @PostMapping("/add-cart")
    public ResponseEntity addCart(@RequestBody AddCart cartForm) {
        return ResponseEntity.ok(cartService.createCart(cartForm));
    }
}
