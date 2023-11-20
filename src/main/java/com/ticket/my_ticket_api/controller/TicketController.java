package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.entity.Ticket;
import com.ticket.my_ticket_api.payload.request.TicketRequest;
import com.ticket.my_ticket_api.service.TicketBuyService.TicketBuyService;
import com.ticket.my_ticket_api.service.TicketBuyService.TicketBuyServiceImpl;
import com.ticket.my_ticket_api.service.ticketService.TicketService;
import com.ticket.my_ticket_api.service.ticketService.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    @Autowired
    final private TicketService ticketService = new TicketServiceImpl();

    @Autowired
    final private TicketBuyService ticketBuyService = new TicketBuyServiceImpl();

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getAllTicketByEventId(@PathVariable("eventId") long eventId) {
        return ticketService.getAllTicketsByEventId(eventId);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<?> createEvent(@PathVariable("eventId") long eventId, @Validated @RequestBody TicketRequest ticketRequest) {
        Ticket ticket = Ticket
                .builder()
                .name(ticketRequest.getName())
                .description(ticketRequest.getDescription())
                .number_place(ticketRequest.getNumber_place())
                .price(ticketRequest.getPrice())
                .visibility(ticketRequest.isVisibility())
                .create_at(new Date())
                .update_at(new Date())
                .build();
        return ticketService.createTicket(ticket, eventId, ticketRequest.getTicketTypeId());
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<?> updateEvent(@PathVariable("ticketId") long ticketId, @Validated @RequestBody TicketRequest ticketRequest) {
        Ticket ticket = Ticket
                .builder()
                .name(ticketRequest.getName())
                .description(ticketRequest.getDescription())
                .number_place(ticketRequest.getNumber_place())
                .price(ticketRequest.getPrice())
                .visibility(ticketRequest.isVisibility())
                .update_at(new Date())
                .build();
        return ticketService.updateSTicket(ticket, ticketId, ticketRequest.getTicketTypeId());
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<?> deleteOneTicket(@PathVariable("ticketId") long ticketId) {
        return ticketService.deleteTicket(ticketId);
    }

    @PostMapping("/paid/{ticketId}/{numberPlace}")
    public ResponseEntity<?> paidTicket(@PathVariable("ticketId") long ticketId, @PathVariable("numberPlace") int numberPlace, @RequestHeader (name="Authorization") String token) {
        return ticketBuyService.createTicketBuy(ticketId, token, numberPlace);
    }

    @GetMapping("/myTicket/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getMyTickets(@RequestHeader (name="Authorization") String token, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize)  {
        System.out.println("token => " + token);
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return ticketBuyService.getTicketBuyByUserId(pageable, token);
    }

    @GetMapping("/myTicket/event/{eventId}")
    public ResponseEntity<?> getMyTicketsByEventId(@RequestHeader (name="Authorization") String token, @PathVariable("eventId") long eventId)  {
        System.out.println("token => " + token);
        return ticketBuyService.getTicketBuyByUserIdAndEventId(eventId, token);
    }
}
