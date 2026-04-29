// process checkout and creates order
package com.example.bookhaven.service;

import com.example.bookhaven.model.User;
import com.example.bookhaven.model.Order;
import com.example.bookhaven.model.Cart;
import com.example.bookhaven.model.CartItems;
import com.example.bookhaven.model.OrderItems;
import com.example.bookhaven.repository.CartRepository;
import com.example.bookhaven.repository.OrderRepository;
import com.example.bookhaven.repository.CartItemsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartItemsRepository cartItemsRepository;

    public OrderService(CartRepository cartRepository, OrderRepository orderRepository, CartItemsRepository cartItemsRepository){
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    public Order checkoutCart(User user) {

        // Get the user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create new Order
        Order order = new Order();
        order.setUser(user);
        double totalPrice = 0;

        // Convert each CartItem → OrderItem
        for (CartItems ci : cart.getCartItems()) {
            OrderItems oi = new OrderItems();
            oi.setOrder(order);
            oi.setBook(ci.getBook());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getBook().getPrice());
            totalPrice += ci.getQuantity() * ci.getBook().getPrice();
            order.getOrder_items().add(oi);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // Clear the cart
        cart.getCartItems().forEach(ci -> cartItemsRepository.delete(ci));

        // Clear list and reset total
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        return order;
    }
}
