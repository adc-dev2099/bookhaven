package com.example.bookhaven.service;

import com.example.bookhaven.model.Book;
import com.example.bookhaven.model.Category;
import com.example.bookhaven.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;
    private Book book3;

    private Category category1;
    private Category category2;

    private Pageable pageable;

    @BeforeEach
    void setUp() {

        category1 = new Category();
        category1.setId(1L);
        category1.setName("Fantasy");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Thriller");

        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("The Lord of the Rings");
        book1.setAuthor("Tolkien");
        book1.setPrice(2000.0);
        book1.setCategories(List.of(category1));

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("The Hobbit");
        book2.setAuthor("Tolkien");
        book2.setPrice(1500.0);
        book2.setCategories(List.of(category1));

        book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Gone Girl");
        book3.setAuthor("Flynn");
        book3.setPrice(900.0);
        book3.setCategories(List.of(category2));

        pageable = PageRequest.of(0, 10);
    }

    // getAllBooks
    @Test
    void getAllBooks() {

        Page<Book> page = new PageImpl<>(List.of(book1, book2, book3));
        when(bookRepository.findAll(pageable)).thenReturn(page);

        Page<Book> result = bookService.getAllBooks(pageable);

        assertThat(result.getContent()).hasSize(3);
    }

    // getBookById
    @Test
    void getBookById_found() {

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Optional<Book> result = bookService.getBookById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void getBookById_notFound() {

        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(99L);

        assertThat(result).isEmpty();
    }

    // searchBooks query only
    @Test
    void searchBooks_queryOnly() {

        Page<Book> page = new PageImpl<>(List.of(book1, book2));

        when(bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        "tolkien",
                        "tolkien",
                        pageable))
                .thenReturn(page);

        Page<Book> result =
                bookService.searchBooks("tolkien", null, pageable);

        assertThat(result.getContent()).hasSize(2);
    }

    // searchBooks query + category
    @Test
    void searchBooks_queryWithCategory() {

        Page<Book> page = new PageImpl<>(List.of(book1, book2));

        when(bookRepository.findDistinctByCategories_Id(1L, pageable))
                .thenReturn(page);

        Page<Book> result =
                bookService.searchBooks("tolkien", 1L, pageable);

        assertThat(result.getContent()).hasSize(2);
    }

    // searchBooksByCategory
    @Test
    void searchBooksByCategory() {

        Page<Book> page = new PageImpl<>(List.of(book1, book2));

        when(bookRepository.findDistinctByCategories_Id(1L, pageable))
                .thenReturn(page);

        Page<Book> result =
                bookService.searchBooksByCategory(1L, pageable);

        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void searchBooksByCategory_empty() {

        when(bookRepository.findDistinctByCategories_Id(99L, pageable))
                .thenReturn(Page.empty());

        Page<Book> result =
                bookService.searchBooksByCategory(99L, pageable);

        assertThat(result.getContent()).isEmpty();
    }

    // pagination
    @Test
    void searchBooks_pagination() {

        Pageable second = PageRequest.of(1,1);

        Page<Book> page = new PageImpl<>(List.of(book1, book2));

        when(bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        "tolkien",
                        "tolkien",
                        second))
                .thenReturn(page);

        Page<Book> result =
                bookService.searchBooks("tolkien", null, second);

        assertThat(result.getContent()).isNotNull();
    }
}