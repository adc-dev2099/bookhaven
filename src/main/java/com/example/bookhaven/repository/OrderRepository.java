// handles order
package com.example.bookhaven.repository;

import com.example.bookhaven.model.Order;
import com.example.bookhaven.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByUserOrderByOrderDateDesc(User user);

    Page<Order> findAll(Pageable pageable);

    @Query("SELECT o FROM Order o WHERE LOWER(o.user.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    Page<Order> findByUsernameContainingIgnoreCase(@Param("username") String username, Pageable pageable);
}
