package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    /* Event */
    List<Ticket> findByEventEventId(long event_id);
    Page<Ticket> findByEventEventId(long event_id, Pageable pageable);
    @Transactional
    void deleteByEventEventId(long event_id);

    /* Ticket Type */
    List<Ticket> findByTicketTypeTicketId(long ticketTypeId);
    @Transactional
    void deleteByTicketTypeTicketId(long ticketTypeId);
}
