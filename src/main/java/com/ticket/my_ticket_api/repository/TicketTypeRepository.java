package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    Optional<TicketType> findByName(String name);
}
