package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    /* Event */
    List<Ticket> findByEventEventId(Long event_id);
    @Transactional
    void deleteByEventEventId(Long event_id);
}
