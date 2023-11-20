package com.ticket.my_ticket_api.service.ticketService;

import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.Ticket;
import com.ticket.my_ticket_api.entity.TicketType;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.payload.response.DataResponse;
import com.ticket.my_ticket_api.payload.response.MessageResponse;
import com.ticket.my_ticket_api.repository.EventRepository;
import com.ticket.my_ticket_api.repository.TicketRepository;
import com.ticket.my_ticket_api.repository.TicketTypeRepository;
import com.ticket.my_ticket_api.repository.UserRepository;
import com.ticket.my_ticket_api.service.EventService.EventService;
import com.ticket.my_ticket_api.service.userService.UserService;
import com.ticket.my_ticket_api.service.userService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService{
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketTypeRepository ticketTypeRepository;
    @Autowired
    private final UserService userService = new UserServiceImpl();

    @Override
    public ResponseEntity<?> createTicket(Ticket ticket, long eventId, long ticketTypeId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Event not found"));
        }

        Optional<TicketType> ticketType = ticketTypeRepository.findById(ticketTypeId);
        if (ticketType.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Type not found"));
        }

        if (ticket.getName().length() < 3) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Ticket name too small"));
        }

        if (ticketRepository.findByEventEventId(eventId).size() > 5 ) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You have more that 5 tickets"));
        }

        ticket.setEvent(event.get());
        ticket.setTicketType(ticketType.get());
        ticketRepository.save(ticket);
        return ResponseEntity
                .ok(new MessageResponse("Ticket created"));
    }

    @Override
    public ResponseEntity<?> getAllTickets() {
        return ResponseEntity.ok(ticketRepository.findAll());
    }

    @Override
    public ResponseEntity<?> getOneTicket(long ticketId) {
        return ResponseEntity
            .ok(ticketRepository.findById(ticketId));
    }

    @Override
    public ResponseEntity<?> updateSTicket(Ticket ticket, long ticketId, long ticketTypeId) {
        Optional<Ticket> ticket1 = ticketRepository.findById(ticketId);
        if (ticket1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Tickets not found"));
        }

        Optional<TicketType> ticketType = ticketTypeRepository.findById(ticketTypeId);
        if (ticketType.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Type not found"));
        }

        ticket1.get().setName(ticket.getName());
        ticket1.get().setDescription(ticket.getDescription());
        ticket1.get().setNumber_place(ticket.getNumber_place());
        ticket1.get().setVisibility(ticket.isVisibility());
        ticket1.get().setPrice(ticket.getPrice());
        ticket1.get().setTicketType(ticketType.get());
        ticketRepository.save(ticket1.get());

        return ResponseEntity
                .ok(new MessageResponse("Ticket updated"));
    }

    @Override
    public ResponseEntity<?> deleteTicket(long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);

        if (ticket.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Tickets not found"));
        }

        ticketRepository.deleteById(ticketId);
        return ResponseEntity
                .ok(new MessageResponse("Ticket deleted"));
    }

    @Override
    public ResponseEntity<?> getAllTicketsByEventId(long eventId) {
        System.out.println(ticketRepository.findByEventEventId(eventId).size());
        System.out.println(eventId);

        return ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(0)
                .dataNumber(ticketRepository.findByEventEventId(eventId).size())
                .data(ticketRepository.findByEventEventId(eventId))
                .build()
        );
    }

    @Override
    public HttpStatus deleteAllTicketsByEventId(long eventId) {
        ticketRepository.deleteByEventEventId(eventId);
        return HttpStatus.OK;
    }
}
