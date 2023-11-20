package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.service.TicketBuyService.TicketBuyService;
import com.ticket.my_ticket_api.service.TicketBuyService.TicketBuyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class TicketBuyController {
    @Autowired
    private final TicketBuyService ticketBuyService = new TicketBuyServiceImpl();

}
