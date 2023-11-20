package com.ticket.my_ticket_api.service.ticketService;

import com.ticket.my_ticket_api.entity.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TicketService {
    public ResponseEntity<?> createTicket(Ticket ticket, long eventId, long ticketTypeId);
    public ResponseEntity<?> getAllTickets();
    public ResponseEntity<?> getOneTicket(long ticketId);
    public ResponseEntity<?> updateSTicket(Ticket ticket, long ticketId, long ticketTypeId);
    public ResponseEntity<?> deleteTicket(long ticketId);
    public ResponseEntity<?> getAllTicketsByEventId(long eventId);
    public HttpStatus deleteAllTicketsByEventId(long eventId);
}
