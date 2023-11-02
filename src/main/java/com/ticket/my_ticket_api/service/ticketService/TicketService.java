package com.ticket.my_ticket_api.service.ticketService;

import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    public HttpStatus createTicket(Ticket ticket);
    public List<Ticket> getAllTickets(Pageable pageable);
    public Optional<Ticket> getOneTicket(long ticketId);
    public HttpStatus updateSTicket(Ticket ticket, long ticketId);
    public HttpStatus deleteTicket(long ticketId);
    public HttpStatus addUserByTicket(long ticketId, long userId);
    public HttpStatus RemoveUserByTicket(long ticketId, long userId);
    public List<Ticket> getAllTicketsByEventId(long eventId);
    public HttpStatus deleteAllTicketsByEventId(long eventId);
}
