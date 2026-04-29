// used when adding items to cart
package com.example.bookhaven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {
    private Long bookId;
    private Integer quantity;
}
