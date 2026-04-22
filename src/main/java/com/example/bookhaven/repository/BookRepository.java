package com.example.bookhaven.repository;

import com.example.bookhaven.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author,
            Pageable pageable
    );

    Page<Book> findDistinctByCategories_Id(
            Long categoryId,
            Pageable pageable
    );

    Page<Book> findDistinctByCategories_IdAndTitleContainingIgnoreCaseOrCategories_IdAndAuthorContainingIgnoreCase(
            Long categoryId1,
            String title,
            Long categoryId2,
            String author,
            Pageable pageable
    );

    boolean existsByCategories_Id(Long id);

    @Query("""
SELECT DISTINCT b FROM Book b
JOIN b.categories c
WHERE c.id = :categoryId
AND (
LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
OR LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%'))
)
""")
    Page<Book> searchByCategoryAndText(
            @Param("categoryId") Long categoryId,
            @Param("search") String search,
            Pageable pageable
    );
}