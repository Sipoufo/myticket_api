package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.service.TicketBuyService.TicketBuyService;
import com.ticket.my_ticket_api.service.TicketBuyService.TicketBuyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ticketBuy")
public class TicketBuyController {
    @Autowired
    private final TicketBuyService ticketBuyService = new TicketBuyServiceImpl();

    @GetMapping("/user/{id}/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getAllTicketBuyByUserId(@PathVariable("id") long id, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        System.out.println("Je passe ticket buy");
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return ticketBuyService.getTicketBuyByUserIs(id, pageable);
    }

    @GetMapping("/ticket/{id}/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getAllTicketBuyByTicketId(@PathVariable("id") long id, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return ticketBuyService.getTicketBuyByTicketId(pageable, id);
    }

    @GetMapping("/numberTicketBuy/{ticketId}")
    public ResponseEntity<?> getNumberTicketBuy(@PathVariable("ticketId") long ticketId) {
        return ticketBuyService.getNumberTicketBuyByTicketId(ticketId);
    }
}
