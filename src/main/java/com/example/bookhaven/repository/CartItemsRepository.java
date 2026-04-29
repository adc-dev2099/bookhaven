// manages cart item
package com.example.bookhaven.repository;

import com.example.bookhaven.model.CartItems;
import com.example.bookhaven.model.Cart;
import com.example.bookhaven.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    Optional<CartItems> findByCartAndBook(Cart cart, Book book);

    List<CartItems> findByCart(Cart cart);
}
