package com.example.bookhaven.service;

import com.example.bookhaven.dto.CartResponseDTO;
import com.example.bookhaven.model.Book;
import com.example.bookhaven.model.Cart;
import com.example.bookhaven.model.CartItems;
import com.example.bookhaven.model.User;
import com.example.bookhaven.repository.BookRepository;
import com.example.bookhaven.repository.CartItemsRepository;
import com.example.bookhaven.repository.CartRepository;
import com.example.bookhaven.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock private CartRepository cartRepository;
    @Mock private CartItemsRepository cartItemsRepository;
    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Book book;
    private Cart cart;
    private CartItems cartItem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        book = new Book();
        book.setId(1L);
        book.setTitle("The Lord of the Rings");
        book.setPrice(2367.00);
        book.setCoverUrl("https://covers.openlibrary.org/test.jpg");

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        cart.setTotalPrice(0.0);

        cartItem = new CartItems();
        cartItem.setCart(cart);
        cartItem.setBook(book);
        cartItem.setQuantity(1);
    }

    // ─── addToCart ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should throw when user not found on addToCart")
    void addToCart_userNotFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addToCart(99L, 1L, 1))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found.");
    }

    @Test
    @DisplayName("Should throw when book not found on addToCart")
    void addToCart_bookNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addToCart(1L, 99L, 1))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found.");
    }

    @Test
    @DisplayName("Should create new cart when user has no cart")
    void addToCart_noExistingCart_createsNewCart() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartItemsRepository.findByCartAndBook(any(), any())).thenReturn(Optional.empty());

        cartService.addToCart(1L, 1L, 1);

        verify(cartRepository, atLeastOnce()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should add new item to existing cart")
    void addToCart_existingCart_newItem_addsItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponseDTO result = cartService.addToCart(1L, 1L, 2);

        assertThat(cart.getCartItems()).hasSize(1);
        assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should increment quantity when item already in cart")
    void addToCart_existingCartItem_incrementsQuantity() {
        cartItem.setQuantity(1);
        cart.getCartItems().add(cartItem);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.addToCart(1L, 1L, 3);

        assertThat(cartItem.getQuantity()).isEqualTo(4);
        verify(cartItemsRepository, times(1)).save(cartItem);
    }

    @Test
    @DisplayName("Should recalculate total after adding item")
    void addToCart_recalculatesTotalPrice() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.addToCart(1L, 1L, 2);

        assertThat(cart.getTotalPrice()).isEqualTo(2367.00 * 2);
    }

    @Test
    @DisplayName("Should return correct DTO after adding item")
    void addToCart_returnsCorrectDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponseDTO result = cartService.addToCart(1L, 1L, 1);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getBookTitle()).isEqualTo("The Lord of the Rings");
    }

    // ─── updateCartItemQuantity ───────────────────────────────────────────────

    @Test
    @DisplayName("Should throw when cart not found on update")
    void updateCartItemQuantity_cartNotFound_throwsException() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateCartItemQuantity(user, 1L, 2))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cart not found");
    }

    @Test
    @DisplayName("Should throw when book not found on update")
    void updateCartItemQuantity_bookNotFound_throwsException() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateCartItemQuantity(user, 99L, 2))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found");
    }

    @Test
    @DisplayName("Should throw when item not in cart on update")
    void updateCartItemQuantity_itemNotInCart_throwsException() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateCartItemQuantity(user, 1L, 2))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Item not in cart");
    }

    @Test
    @DisplayName("Should update quantity when valid quantity provided")
    void updateCartItemQuantity_validQuantity_updatesQuantity() {
        cart.getCartItems().add(cartItem);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.updateCartItemQuantity(user, 1L, 5);

        assertThat(cartItem.getQuantity()).isEqualTo(5);
        verify(cartItemsRepository, times(1)).save(cartItem);
    }

    @Test
    @DisplayName("Should remove item when quantity is zero")
    void updateCartItemQuantity_zeroQuantity_removesItem() {
        cart.getCartItems().add(cartItem);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.updateCartItemQuantity(user, 1L, 0);

        verify(cartItemsRepository, times(1)).delete(cartItem);
        assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("Should remove item when quantity is negative")
    void updateCartItemQuantity_negativeQuantity_removesItem() {
        cart.getCartItems().add(cartItem);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.updateCartItemQuantity(user, 1L, -1);

        verify(cartItemsRepository, times(1)).delete(cartItem);
        assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("Should recalculate total after updating quantity")
    void updateCartItemQuantity_recalculatesTotalPrice() {
        cart.getCartItems().add(cartItem);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.updateCartItemQuantity(user, 1L, 3);

        assertThat(cart.getTotalPrice()).isEqualTo(2367.00 * 3);
    }

    // ─── removeItemFromCart ───────────────────────────────────────────────────

    @Test
    @DisplayName("Should throw when cart not found on remove")
    void removeItemFromCart_cartNotFound_throwsException() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.removeItemFromCart(user, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cart not found");
    }

    @Test
    @DisplayName("Should throw when book not found on remove")
    void removeItemFromCart_bookNotFound_throwsException() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.removeItemFromCart(user, 99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found");
    }

    @Test
    @DisplayName("Should throw when item not in cart on remove")
    void removeItemFromCart_itemNotInCart_throwsException() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.removeItemFromCart(user, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Item not in cart");
    }

    @Test
    @DisplayName("Should remove item from cart successfully")
    void removeItemFromCart_validItem_removesItem() {
        cart.getCartItems().add(cartItem);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.removeItemFromCart(user, 1L);

        verify(cartItemsRepository, times(1)).delete(cartItem);
        assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("Should recalculate total to zero after removing only item")
    void removeItemFromCart_recalculatesTotalToZero() {
        cart.getCartItems().add(cartItem);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.removeItemFromCart(user, 1L);

        assertThat(cart.getTotalPrice()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should return correct DTO after removing item")
    void removeItemFromCart_returnsCorrectDTO() {
        cart.getCartItems().add(cartItem);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponseDTO result = cartService.removeItemFromCart(user, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalPrice()).isEqualTo(0.0);
    }
}