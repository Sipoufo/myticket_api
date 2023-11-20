package com.ticket.my_ticket_api.service.ticketTypeService;

import com.ticket.my_ticket_api.entity.Ticket;
import com.ticket.my_ticket_api.entity.TicketType;
import org.springframework.http.ResponseEntity;

public interface TicketTypeService {
    public ResponseEntity<?> createTicketType(TicketType ticketType);
    public ResponseEntity<?> getAllTicketsType();
    public ResponseEntity<?> getOneTicketType(long ticketTypeId);
    public ResponseEntity<?> updateSTicketType(TicketType ticketType, long ticketTypeId);
    public ResponseEntity<?> deleteTicketType(long ticketTypeId);
}
