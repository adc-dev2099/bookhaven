// handles retrieving books and admin book management
package com.example.bookhaven.service;

import com.example.bookhaven.model.Book;
import com.example.bookhaven.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // search query only
    public Page<Book> searchBooks(String query, Pageable pageable) {
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        query,
                        query,
                        pageable
                );
    }

    // category only
    public Page<Book> searchBooksByCategory(Long categoryId, Pageable pageable) {
        return bookRepository
                .findDistinctByCategories_Id(
                        categoryId,
                        pageable
                );
    }

    // search + category
    public Page<Book> searchBooks(String query, Long categoryId, Pageable pageable) {

        if (categoryId != null) {
            return bookRepository
                    .findDistinctByCategories_Id(
                            categoryId,
                            pageable
                    );
        }

        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        query,
                        query,
                        pageable
                );
    }
}