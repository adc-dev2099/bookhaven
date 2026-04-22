package com.example.bookhaven.service;

import com.example.bookhaven.dto.CartResponseDTO;
import com.example.bookhaven.model.User;
import com.example.bookhaven.model.Book;
import com.example.bookhaven.model.Cart;
import com.example.bookhaven.model.CartItems;
import com.example.bookhaven.repository.BookRepository;
import com.example.bookhaven.repository.CartItemsRepository;
import com.example.bookhaven.repository.CartRepository;
import com.example.bookhaven.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemsRepository cartItemsRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public CartResponseDTO addToCart(Long userId, Long bookId, int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found."));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItems cartItem = cartItemsRepository.findByCartAndBook(cart, book).orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemsRepository.save(cartItem);
        } else {
            cartItem = new CartItems();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);

            cart.getCartItems().add(cartItem);
        }

        recalculateCartTotal(cart);
        cartRepository.save(cart);
        return mapCartToDTO(cart);
    }

    public CartResponseDTO updateCartItemQuantity(User user, Long bookId, int quantity) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        CartItems cartItem = cartItemsRepository
                .findByCartAndBook(cart, book)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        if (quantity <= 0) {
            cartItemsRepository.delete(cartItem);
            cart.getCartItems().remove(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemsRepository.save(cartItem);
            if (!cart.getCartItems().contains(cartItem)) {
                cart.getCartItems().add(cartItem);
            }
        }

        recalculateCartTotal(cart);
        cartRepository.save(cart);
        return mapCartToDTO(cart);
    }

    public CartResponseDTO removeItemFromCart(User user, Long bookId) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        CartItems cartItem = cartItemsRepository
                .findByCartAndBook(cart, book)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        cartItemsRepository.delete(cartItem);
        cart.getCartItems().remove(cartItem);
        recalculateCartTotal(cart);
        cartRepository.save(cart);

        return mapCartToDTO(cart);
    }

    private void recalculateCartTotal(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(ci -> ci.getBook().getPrice() * ci.getQuantity())
                .sum();
        cart.setTotalPrice(total);
    }

    // Map Cart entity to DTO
    private CartResponseDTO mapCartToDTO(Cart cart) {
        List<CartResponseDTO.CartItemDTO> items = cart.getCartItems().stream()
                .map(ci -> new CartResponseDTO.CartItemDTO(
                        ci.getBook().getId(),
                        ci.getBook().getTitle(),
                        ci.getBook().getCoverUrl(),
                        ci.getBook().getPrice(),
                        ci.getQuantity()
                ))
                .toList();

        return new CartResponseDTO(
                cart.getId(),
                cart.getUser().getId(),
                cart.getTotalPrice(),
                items
        );
    }
}
