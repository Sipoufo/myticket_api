package com.ticket.my_ticket_api.service.TicketBuyService;

import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.TicketBuy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TicketBuyService {
    public ResponseEntity<?> createTicketBuy(long ticketId, String token, int numberPlace);
    public ResponseEntity<?> getTicketBuyByUserId(Pageable pageable, String token);
    public ResponseEntity<?> getTicketBuyByUserIdAndEventId(long eventId, String token);
    public ResponseEntity<?> getTicketBuyByTicketId(Pageable pageable, long ticketId);
}
