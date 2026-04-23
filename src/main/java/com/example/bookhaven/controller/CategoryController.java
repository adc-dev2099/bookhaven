package com.example.bookhaven.controller;

import com.example.bookhaven.model.Category;
import com.example.bookhaven.repository.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> getCategories() {
        return categoryRepository.findAll(
                Sort.by("name").ascending()
        );
    }
}