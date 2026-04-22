package com.example.bookhaven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
    private Long cartId;
    private Long userId;
    private Double totalPrice;
    private List<CartItemDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemDTO {
        private Long bookId;
        private String bookTitle;
        private String coverURL;
        private Double bookPrice;
        private Integer quantity;
    }
}
