// Allows admin users to add, update, delete books.
package com.example.bookhaven.controller;

import com.example.bookhaven.model.Book;
import com.example.bookhaven.model.Category;
import com.example.bookhaven.model.Order;
import com.example.bookhaven.model.User;
import com.example.bookhaven.repository.BookRepository;
import com.example.bookhaven.repository.CategoryRepository;
import com.example.bookhaven.repository.UserRepository;
import com.example.bookhaven.repository.OrderRepository;
import com.example.bookhaven.dto.AdminBookRequestDTO;
import com.example.bookhaven.dto.RegisterRequestDTO;
import com.example.bookhaven.dto.OrderResponseDTO;
import com.example.bookhaven.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminController(
            BookRepository bookRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            OrderRepository orderRepository,
            UserService userService,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= BOOKS =================

    @GetMapping("/books")
    public Page<Book> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId
    ) {
        PageRequest pageable = PageRequest.of(page, size);

        if (search != null && !search.isBlank() && categoryId != null) {
            return bookRepository.searchByCategoryAndText(categoryId, search, pageable);
        }

        if (search != null && !search.isBlank()) {
            return bookRepository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrderByTitleAsc(
                            search, search, pageable
                    );
        }

        if (categoryId != null) {
            return bookRepository
                    .findDistinctByCategories_IdOrderByTitleAsc(categoryId, pageable);
        }

        return bookRepository.findAll(
                PageRequest.of(page, size, Sort.by("title").ascending())
        );
    }

    // ================= ADD BOOK =================
    @PostMapping("/books")
    public ResponseEntity<?> addBook(@Valid @RequestBody AdminBookRequestDTO request) {

        if (request.getCategoryIds() == null ||
                request.getCategoryIds().isEmpty() ||
                request.getCategoryIds().size() > 3) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Book must have 1 to 3 categories"));
        }

        List<Category> categories =
                categoryRepository.findAllById(request.getCategoryIds());

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice()); // validated (no negative allowed)
        book.setDescription(request.getDescription());
        book.setCoverUrl(request.getCoverUrl());
        book.setCategories(categories);

        return ResponseEntity.ok(bookRepository.save(book));
    }

    // ================= UPDATE BOOK =================
    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody AdminBookRequestDTO request
    ) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (request.getCategoryIds() == null ||
                request.getCategoryIds().isEmpty() ||
                request.getCategoryIds().size() > 3) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Book must have 1 to 3 categories"));
        }

        List<Category> categories =
                categoryRepository.findAllById(request.getCategoryIds());

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice()); // validated
        book.setDescription(request.getDescription());
        book.setCoverUrl(request.getCoverUrl());
        book.setCategories(categories);

        return ResponseEntity.ok(bookRepository.save(book));
    }

    // ================= DELETE BOOK =================
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        bookRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Book deleted successfully"));
    }

    // ================= CATEGORIES =================

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by("name").ascending());
    }

    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody Map<String, String> body) {

        String name = body.get("name");

        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Category name cannot be empty."));
        }

        if (categoryRepository.findByNameIgnoreCase(name).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Category already exists."));
        }

        Category category = new Category();
        category.setName(name.trim());

        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String name = body.get("name");

        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Category name cannot be empty."));
        }

        if (categoryRepository.findByNameIgnoreCase(name)
                .filter(c -> !c.getId().equals(id))
                .isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Category name already exists."));
        }

        category.setName(name.trim());
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {

        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (bookRepository.existsByCategories_Id(id)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Cannot delete category with existing books."));
        }

        categoryRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Category deleted successfully"));
    }

    // ================= USERS =================

    @GetMapping("/users")
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        if (search != null && !search.isBlank()) {
            return userRepository
                    .findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                            search, search, search, pageable
                    );
        }

        return userRepository.findAll(pageable);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody RegisterRequestDTO request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Username already exists."));
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "user");

        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, body));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PatchMapping("/users/{id}/toggle-active")
    public ResponseEntity<?> toggleUserActive(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getId() == 1) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "This account cannot be deactivated."));
        }

        user.setActive(!user.isActive());
        userRepository.save(user);

        String status = user.isActive() ? "activated" : "deactivated";

        return ResponseEntity.ok(
                Map.of(
                        "message", "User " + status + " successfully",
                        "active", user.isActive()
                )
        );
    }

    // ================= ORDERS =================

    @GetMapping("/orders")
    public Page<OrderResponseDTO> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {

        PageRequest pageable =
                PageRequest.of(page, size, Sort.by("orderDate").descending());

        Page<Order> orderPage =
                (search != null && !search.isBlank())
                        ? orderRepository.findByUsernameContainingIgnoreCase(search, pageable)
                        : orderRepository.findAll(pageable);

        return orderPage.map(order -> {

            List<OrderResponseDTO.OrderItemDTO> items =
                    order.getOrder_items().stream()
                            .map(oi -> new OrderResponseDTO.OrderItemDTO(
                                    oi.getBook().getId(),
                                    oi.getBook().getTitle(),
                                    oi.getQuantity(),
                                    oi.getPrice(),
                                    oi.getBook().getCoverUrl()
                            ))
                            .toList();

            return new OrderResponseDTO(
                    order.getId(),
                    items,
                    order.getTotalPrice(),
                    order.getOrderDate(),
                    order.getUser().getUsername()
            );
        });
    }

    // ================= DASHBOARD STATS =================

    @GetMapping("/stats")
    public Map<String, Long> getDashboardStats() {
        return Map.of(
                "books", bookRepository.count(),
                "categories", categoryRepository.count(),
                "users", userRepository.count(),
                "orders", orderRepository.count()
        );
    }
}