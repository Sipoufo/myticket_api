package com.ticket.my_ticket_api.service.TicketBuyService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TicketBuyService {
    public ResponseEntity<?> createTicketBuy(long ticketId, String token, int numberPlace);
    public ResponseEntity<?> getTicketBuyByToken(Pageable pageable, String token);
    public ResponseEntity<?> getTicketBuyByUserIs(long userId, Pageable pageable);
    public ResponseEntity<?> getTicketBuyByUserIdAndEventId(long eventId, String token);
    public ResponseEntity<?> getTicketBuyByTicketId(Pageable pageable, long ticketId);
    public ResponseEntity<?> getNumberTicketBuyByTicketId(long ticketId);
}
