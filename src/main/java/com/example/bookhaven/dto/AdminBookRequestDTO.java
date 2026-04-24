package com.example.bookhaven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminBookRequestDTO {

    private String title;
    private String author;
    private Double price;
    private String description;

    // CHANGE TO LIST
    private List<Long> categoryIds;

    private String coverUrl;


}