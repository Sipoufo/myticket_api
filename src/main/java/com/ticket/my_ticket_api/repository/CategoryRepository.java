package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Category;
import com.ticket.my_ticket_api.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
