package com.example.bookhaven.service;

import com.example.bookhaven.model.*;
import com.example.bookhaven.repository.CartItemsRepository;
import com.example.bookhaven.repository.CartRepository;
import com.example.bookhaven.repository.OrderRepository;
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
class OrderServiceTest {

    @Mock private CartRepository cartRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private CartItemsRepository cartItemsRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Book book1;
    private Book book2;
    private Cart cart;
    private CartItems cartItem1;
    private CartItems cartItem2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("The Lord of the Rings");
        book1.setPrice(2367.00);

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("The Hobbit");
        book2.setPrice(1500.00);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        cart.setTotalPrice(0.0);

        cartItem1 = new CartItems();
        cartItem1.setCart(cart);
        cartItem1.setBook(book1);
        cartItem1.setQuantity(2);

        cartItem2 = new CartItems();
        cartItem2.setCart(cart);
        cartItem2.setBook(book2);
        cartItem2.setQuantity(1);
    }

    // ─── Cart Not Found ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Should throw when cart not found")
    void checkoutCart_cartNotFound_throwsException() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.checkoutCart(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cart is empty");
    }

    // ─── Empty Cart ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should throw when cart has no items")
    void checkoutCart_emptyCart_throwsException() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        assertThatThrownBy(() -> orderService.checkoutCart(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cart is empty");
    }

    // ─── Successful Checkout ──────────────────────────────────────────────────

    @Test
    @DisplayName("Should return order on successful checkout")
    void checkoutCart_validCart_returnsOrder() {
        cart.getCartItems().add(cartItem1);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.checkoutCart(user);

        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should create order with correct total price for single item")
    void checkoutCart_singleItem_correctTotalPrice() {
        cartItem1.setQuantity(2);
        cart.getCartItems().add(cartItem1);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.checkoutCart(user);

        assertThat(result.getTotalPrice()).isEqualTo(2367.00 * 2);
    }

    @Test
    @DisplayName("Should create order with correct total price for multiple items")
    void checkoutCart_multipleItems_correctTotalPrice() {
        cart.getCartItems().add(cartItem1);
        cart.getCartItems().add(cartItem2);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.checkoutCart(user);

        double expected = (2367.00 * 2) + (1500.00 * 1);
        assertThat(result.getTotalPrice()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should create order items from cart items")
    void checkoutCart_validCart_createsOrderItems() {
        cart.getCartItems().add(cartItem1);
        cart.getCartItems().add(cartItem2);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.checkoutCart(user);

        assertThat(result.getOrder_items()).hasSize(2);
    }

    @Test
    @DisplayName("Should map cart item book and quantity to order item correctly")
    void checkoutCart_validCart_orderItemsHaveCorrectData() {
        cart.getCartItems().add(cartItem1);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.checkoutCart(user);

        OrderItems orderItem = result.getOrder_items().get(0);
        assertThat(orderItem.getBook()).isEqualTo(book1);
        assertThat(orderItem.getQuantity()).isEqualTo(2);
        assertThat(orderItem.getPrice()).isEqualTo(2367.00);
    }

    @Test
    @DisplayName("Should save order to repository")
    void checkoutCart_validCart_savesOrder() {
        cart.getCartItems().add(cartItem1);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        orderService.checkoutCart(user);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    // ─── Cart Clearing ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should delete all cart items after checkout")
    void checkoutCart_validCart_deletesCartItems() {
        cart.getCartItems().add(cartItem1);
        cart.getCartItems().add(cartItem2);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        orderService.checkoutCart(user);

        verify(cartItemsRepository, times(1)).delete(cartItem1);
        verify(cartItemsRepository, times(1)).delete(cartItem2);
    }

    @Test
    @DisplayName("Should clear cart items list after checkout")
    void checkoutCart_validCart_clearsCartItemsList() {
        cart.getCartItems().add(cartItem1);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        orderService.checkoutCart(user);

        assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("Should reset cart total price to zero after checkout")
    void checkoutCart_validCart_resetsCartTotalToZero() {
        cart.getCartItems().add(cartItem1);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        orderService.checkoutCart(user);

        assertThat(cart.getTotalPrice()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should save cart after clearing")
    void checkoutCart_validCart_savesCartAfterClearing() {
        cart.getCartItems().add(cartItem1);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        orderService.checkoutCart(user);

        verify(cartRepository, times(1)).save(cart);
    }
}