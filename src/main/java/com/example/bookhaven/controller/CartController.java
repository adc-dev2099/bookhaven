// Handles adding items to cart, updating quantity, and viewing cart items
package com.example.bookhaven.controller;

import com.example.bookhaven.dto.CartRequestDTO;
import com.example.bookhaven.dto.CartResponseDTO;
import com.example.bookhaven.model.Cart;
import com.example.bookhaven.model.User;
import com.example.bookhaven.repository.CartItemsRepository;
import com.example.bookhaven.repository.CartRepository;
import com.example.bookhaven.repository.UserRepository;
import com.example.bookhaven.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;

    public CartController(CartService cartService, UserRepository userRepository, CartRepository cartRepository, CartItemsRepository cartItemsRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    @GetMapping
    public ResponseEntity<?> getMyCart(Principal principal) {
        try {
            String username = principal.getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Cart cart = cartRepository.findByUser(user).orElse(null);

            if (cart == null) {
                return ResponseEntity.ok(new CartResponseDTO(null, user.getId(), 0.0, List.of()));
            }

            // Eagerly load items within the same transaction via cartItemsRepository
            List<CartResponseDTO.CartItemDTO> items = cartItemsRepository.findByCart(cart).stream()
                    .map(ci -> new CartResponseDTO.CartItemDTO(
                            ci.getBook().getId(),
                            ci.getBook().getTitle(),
                            ci.getBook().getCoverUrl(),
                            ci.getBook().getPrice(),
                            ci.getQuantity()
                    ))
                    .toList();

            return ResponseEntity.ok(new CartResponseDTO(
                    cart.getId(),
                    cart.getUser().getId(),
                    cart.getTotalPrice(),
                    items
            ));

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@RequestBody CartRequestDTO request, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            CartResponseDTO result = cartService.addToCart(user.getId(), request.getBookId(), request.getQuantity());
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/my-cart/{bookId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long bookId, @RequestParam int quantity, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            CartResponseDTO result = cartService.updateCartItemQuantity(user, bookId, quantity);
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/my-cart/{bookId}")
    public ResponseEntity<?> removeItem(@PathVariable Long bookId, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cartService.removeItemFromCart(user, bookId);
            return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}
