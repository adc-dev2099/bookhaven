// return order details
package com.example.bookhaven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private List<OrderItemDTO> items;
    private Double totalPrice;
    private LocalDateTime orderDate;
    private String username;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long bookId;
        private String title;
        private Integer quantity;
        private Double price;
        private String coverURL;
    }
}