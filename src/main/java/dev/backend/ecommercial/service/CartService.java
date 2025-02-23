package dev.backend.ecommercial.service;

import dev.backend.ecommercial.model.dto.CartDto;
import dev.backend.ecommercial.model.entity.Cart;
import dev.backend.ecommercial.model.entity.Product;
import dev.backend.ecommercial.model.entity.User;
import dev.backend.ecommercial.model.payload.request.AddCart;
import dev.backend.ecommercial.model.payload.response.ResponseData;
import dev.backend.ecommercial.repository.CartRepository;
import dev.backend.ecommercial.repository.ProductRepository;
import dev.backend.ecommercial.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

        public ResponseData<CartDto> createCart(AddCart cartForm) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User currentUser = userRepository.findByEmail(email).orElse(null);

            if(currentUser == null) {
                return new ResponseData<>(400, "User not found with email: " + email, null);
            }

            Product product = productRepository.findById(cartForm.getProductId()).orElse(null);
            if (product == null) {
                return new ResponseData<>(400, "Product not found with id: " + cartForm.getProductId(), null);
            }


            Cart cart = new Cart();
            cart.setUser(currentUser);
            cart.setProduct(product);
            cart.setQuantity(cartForm.getQuantity());


            Cart savedCart = cartRepository.save(cart);

            return new ResponseData<>(200, "Cart created successfully", CartDto.from(savedCart));
        }

    public ResponseData<CartDto> updateCart(AddCart cartForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email).orElse(null);

        if(currentUser == null) {
            return new ResponseData<>(400, "User not found with email: " + email, null);
        }

        Cart cart = cartRepository.findByUserAndProduct(currentUser, productRepository.findById(cartForm.getProductId()).orElse(null));
        if(cart == null) {
            return new ResponseData<>(400, "Cart not found", null);
        }
        cart.setQuantity(cartForm.getQuantity());
        Cart savedCart = cartRepository.save(cart);
        return new ResponseData<>(200, "Cart updated successfully", CartDto.from(savedCart));
    }

    public ResponseData<CartDto> deleteCart(AddCart cartForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email).orElse(null);

        if(currentUser == null) {
            return new ResponseData<>(400, "User not found with email: " + email, null);
        }

        Cart cart = cartRepository.findByUserAndProduct(currentUser, productRepository.findById(cartForm.getProductId()).orElse(null));
        if(cart == null) {
            return new ResponseData<>(400, "Cart not found", null);
        }
        cartRepository.delete(cart);
        return new ResponseData<>(200, "Cart deleted successfully", CartDto.from(cart));
    }

    public ResponseData<List<CartDto>> getAllProductForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartDto> listCarts = cartRepository.findAllByUser(user).stream()
                .map(CartDto::from)
                .collect(Collectors.toList());
        return new ResponseData<>(200, "Retrieved all carts successfully", listCarts);
    }

    public ResponseData<CartDto> addToCart(AddCart cartForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email).orElse(null);

        if (currentUser == null) {
            return new ResponseData<>(400, "User not found with email: " + email, null);
        }

        Product product = productRepository.findById(cartForm.getProductId()).orElse(null);
        if (product == null) {
            return new ResponseData<>(400, "Product not found", null);
        }

        Cart cart = cartRepository.findByUserAndProduct(currentUser, productRepository.findById(cartForm.getProductId()).orElse(null));
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + cartForm.getQuantity());
        }else {
            cart = new Cart();
            cart.setUser(currentUser);
            cart.setProduct(productRepository.findById(cartForm.getProductId()).orElse(null));
            cart.setQuantity(cartForm.getQuantity());
        }

        cartRepository.save(cart);
        return new ResponseData<>(200, "Product added to cart successfully", CartDto.from(cart));

    }

    @Transactional
    public ResponseData<String> checkout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartRepository.deleteAllByUser(user);

        return new ResponseData<>(200, "Checkout successful! Cart is now empty.", null);
    }
}
