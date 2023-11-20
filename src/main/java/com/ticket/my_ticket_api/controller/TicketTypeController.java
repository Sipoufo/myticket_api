package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.entity.TicketType;
import com.ticket.my_ticket_api.payload.request.TicketTypeRequest;
import com.ticket.my_ticket_api.service.ticketTypeService.TicketTypeService;
import com.ticket.my_ticket_api.service.ticketTypeService.TicketTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ticketType")
public class TicketTypeController {
    @Autowired
    final private TicketTypeService ticketTypeService = new TicketTypeServiceImpl();

    @GetMapping("")
    public ResponseEntity<?> getAllTicketTypes() {
        return ticketTypeService.getAllTicketsType();
    }

    @GetMapping("/{ticketTypeId}")
    public ResponseEntity<?> getTicketType(@PathVariable("ticketTypeId") long ticketTypeId) {
        return ticketTypeService.getOneTicketType(ticketTypeId);
    }

    @PostMapping("")
    public ResponseEntity<?> createTicketType(@Validated @RequestBody TicketTypeRequest ticketTypeRequest) {
        TicketType ticketType = TicketType
                .builder()
                .name(ticketTypeRequest.getName())
                .build();
        return ticketTypeService.createTicketType(ticketType);
    }

    @PutMapping("/{ticketTypeId}")
    public ResponseEntity<?> updateTicketType(@PathVariable("ticketTypeId") long ticketTypeId, @Validated @RequestBody TicketTypeRequest ticketTypeRequest) {
        TicketType ticketType = TicketType
                .builder()
                .name(ticketTypeRequest.getName())
                .build();
        return ticketTypeService.updateSTicketType(ticketType, ticketTypeId);
    }

    @DeleteMapping("/{ticketTypeId}")
    public ResponseEntity<?> deleteTicketType(@PathVariable("ticketTypeId") long ticketTypeId) {
        return ticketTypeService.deleteTicketType(ticketTypeId);
    }
}
