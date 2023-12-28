package com.ticket.my_ticket_api.service.TicketBuyService;

import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.Ticket;
import com.ticket.my_ticket_api.entity.TicketBuy;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.payload.response.*;
import com.ticket.my_ticket_api.repository.EventRepository;
import com.ticket.my_ticket_api.repository.TicketBuyRepository;
import com.ticket.my_ticket_api.repository.TicketRepository;
import com.ticket.my_ticket_api.repository.UserRepository;
import com.ticket.my_ticket_api.service.userService.UserService;
import com.ticket.my_ticket_api.service.userService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketBuyServiceImpl implements TicketBuyService{
    @Autowired
    private TicketBuyRepository ticketBuyRepository;
    @Autowired
    private final UserService userService = new UserServiceImpl();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EventRepository eventRepository;

    @Override
    public ResponseEntity<?> createTicketBuy(long ticketId, String token, int numberPlace) {
        Optional<Users> user = userService.getUserByToken(token);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User not found !"));
        }
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Ticket not found !"));
        }

        Pageable pageable = PageRequest.of(1, ticket.get().getNumber_place());
        List<UserTicketBuy> ticketBuys = ticketBuyRepository.findByTicketTicketId(ticket.get().getTicketId(), pageable);
        if (ticketBuys.size() + numberPlace > ticket.get().getNumber_place()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Only "+ (ticket.get().getNumber_place() - ticketBuys.size()) +" ticket left !"));
        }
        for (int i = 0; i < numberPlace; i++) {
            TicketBuy ticketBuy = TicketBuy
                .builder()
                .user(user.get())
                .ticket(ticket.get())
                .build();
            ticketBuyRepository.save(ticketBuy);
        }

        return ResponseEntity.ok(new MessageResponse("Ticket successful buy !"));
    }

    @Override
    public ResponseEntity<?> getTicketBuyByToken(Pageable pageable, String token) {
        Optional<Users> user = userService.getUserByToken(token);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User not found !"));
        }

        return ResponseEntity.ok(
                DataResponse
                    .builder()
                    .actualPage(pageable.getPageNumber() + 1)
                    .dataNumber(ticketBuyRepository.findByUserUserId(user.get().getUserId(), pageable).size())
                    .data(ticketBuyRepository.findByUserUserId(user.get().getUserId(), pageable))
                    .build()
        );
    }

    @Override
    public ResponseEntity<?> getTicketBuyByUserIs(long userId, Pageable pageable) {
        Optional<Users> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User not found !"));
        }

        return ResponseEntity.ok(
                DataResponse
                        .builder()
                        .actualPage(pageable.getPageNumber() + 1)
                        .dataNumber(ticketBuyRepository.findByUserUserId(user.get().getUserId(), pageable).size())
                        .data(ticketBuyRepository.findByUserUserId(user.get().getUserId(), pageable))
                        .pageable(pageable)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getTicketBuyByUserIdAndEventId(long eventId, String token) {
        Optional<Users> user = userService.getUserByToken(token);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User not found !"));
        }

        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Event not found !"));
        }

        List<Ticket> tickets = ticketRepository.findByEventEventId(eventId);

        List<OneTicketResponse> ticketLists = new ArrayList<>();

        for (Ticket ticket : tickets) {
            List<TicketBuy> ticketBuys = ticketBuyRepository.findByTicketTicketIdAndUserUserId(ticket.getTicketId(), user.get().getUserId());
            if (!ticketBuys.isEmpty()) {
                OneTicketResponse oneTicketResponse = OneTicketResponse
                        .builder()
                        .ticketNumber(ticketBuys.size())
                        .ticket(ticket)
                        .build();
                ticketLists.add(oneTicketResponse);
            }
        }

        return ResponseEntity.ok(
                TicketEventResponse
                        .builder()
                        .tickets(ticketLists)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getTicketBuyByTicketId(Pageable pageable, long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Ticket not found !"));
        }

        List<UserTicketBuy> userTicketBuyResponses = ticketBuyRepository.findByTicketTicketId(ticket.get().getTicketId(), pageable);

        List<UserTicketBuyResponse> ticketBuys = new ArrayList<>();

        for (int i = 0; i < userTicketBuyResponses.size(); i++) {
            Optional<Users> users = userRepository.findById(userTicketBuyResponses.get(i).getUserId());
            if (users.isPresent()) {
                ticketBuys.add(UserTicketBuyResponse
                        .builder()
                                .ticketCount(userTicketBuyResponses.get(i).getTicketCount())
                                .user(users.get())
                        .build());
            }
        }

        return ResponseEntity.ok(
                DataResponse
                        .builder()
                        .actualPage(pageable.getPageNumber() + 1)
                        .dataNumber(ticketBuys.size())
                        .data(ticketBuys)
                        .build()
        );
    }
}
