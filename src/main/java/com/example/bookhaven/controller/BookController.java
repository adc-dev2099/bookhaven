package com.example.bookhaven.controller;

import com.example.bookhaven.model.Book;
import com.example.bookhaven.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Get all books
    @GetMapping
    public Page<Book> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId
    ) {
        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by("title").ascending()
        );

        if (q != null && !q.isBlank()) {
            // search with optional category filter
            return bookService.searchBooks(q, categoryId, pageable);
        }

        if (categoryId != null) {
            return bookService.searchBooksByCategory(categoryId, pageable);
        }

        return bookService.getAllBooks(pageable);
    }

    // Get book by id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Search books
    @GetMapping("/search")
    public Page<Book> searchBooks(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookService.searchBooks(query, PageRequest.of(page, size));
    }

    // Filter books by category
    @GetMapping("/filter")
    public Page<Book> filterBooksByCategory(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookService.searchBooksByCategory(categoryId, PageRequest.of(page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}


