// retrieves books
package com.example.bookhaven.repository;

import com.example.bookhaven.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Search
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author,
            Pageable pageable
    );

    // Search (A-Z)
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrderByTitleAsc(
            String title,
            String author,
            Pageable pageable
    );

    // Filter by category
    Page<Book> findDistinctByCategories_Id(
            Long categoryId,
            Pageable pageable
    );

    // Filter by category (A-Z)
    Page<Book> findDistinctByCategories_IdOrderByTitleAsc(
            Long categoryId,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT b FROM Book b
        JOIN b.categories c
        WHERE c.id = :categoryId
        AND (
            LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        ORDER BY b.title ASC
    """)
    Page<Book> searchByCategoryAndText(
            @Param("categoryId") Long categoryId,
            @Param("search") String search,
            Pageable pageable
    );

    boolean existsByCategories_Id(Long id);
}