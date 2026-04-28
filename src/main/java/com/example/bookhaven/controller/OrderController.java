// Handles checkout and order history
package com.example.bookhaven.controller;

import com.example.bookhaven.dto.OrderResponseDTO;
import com.example.bookhaven.model.Order;
import com.example.bookhaven.model.User;
import com.example.bookhaven.repository.OrderRepository;
import com.example.bookhaven.repository.UserRepository;
import com.example.bookhaven.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, UserRepository userRepository, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    private List<OrderResponseDTO.OrderItemDTO> mapOrderItems(Order order) {
        return order.getOrder_items().stream()
                .map(oi -> new OrderResponseDTO.OrderItemDTO(
                        oi.getBook().getId(),
                        oi.getBook().getTitle(),
                        oi.getQuantity(),
                        oi.getPrice(),
                        oi.getBook().getCoverUrl()
                ))
                .toList();
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> getOrdersForUser(Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);

            List<OrderResponseDTO> result = orders.stream()
                    .map(order -> new OrderResponseDTO(
                            order.getId(),
                            mapOrderItems(order),
                            order.getTotalPrice(),
                            order.getOrderDate(),
                            null
                    ))
                    .toList();

            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Order order = orderService.checkoutCart(user);

            OrderResponseDTO result = new OrderResponseDTO(
                    order.getId(),
                    mapOrderItems(order),
                    order.getTotalPrice(),
                    order.getOrderDate(),
                    null
            );

            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}
