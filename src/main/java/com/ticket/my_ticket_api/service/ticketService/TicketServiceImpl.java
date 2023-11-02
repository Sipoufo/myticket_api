package com.ticket.my_ticket_api.service.ticketService;

import com.ticket.my_ticket_api.entity.Ticket;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.repository.TicketRepository;
import com.ticket.my_ticket_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService{
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public HttpStatus createTicket(Ticket ticket) {
        if (ticket.getName().length() < 3) {
            return HttpStatus.NOT_IMPLEMENTED;
        }
        ticketRepository.save(ticket);
        return HttpStatus.CREATED;
    }

    @Override
    public List<Ticket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable).getContent();
    }

    @Override
    public Optional<Ticket> getOneTicket(long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    @Override
    public HttpStatus updateSTicket(Ticket ticket, long ticketId) {
        Optional<Ticket> ticket1 = ticketRepository.findById(ticketId);

        if (ticket1.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        ticket1.get().setName(ticket.getName());
        ticket1.get().setDescription(ticket.getDescription());
        ticket1.get().setNumber_place(ticket.getNumber_place());
        ticket1.get().setVisibility(ticket.isVisibility());
        ticket1.get().setPrice(ticket.getPrice());
        ticketRepository.save(ticket1.get());

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus deleteTicket(long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);

        if (ticket.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        ticketRepository.deleteById(ticketId);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus addUserByTicket(long ticketId, long userId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);

        if (ticket.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        Optional<Users> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        ticket.get().addUser(user.get());
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus RemoveUserByTicket(long ticketId, long userId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);

        if (ticket.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        Optional<Users> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        ticket.get().removeUser(user.get().getUserId());
        return HttpStatus.OK;
    }

    @Override
    public List<Ticket> getAllTicketsByEventId(long eventId) {
        return ticketRepository.findByEventEventId(eventId);
    }

    @Override
    public HttpStatus deleteAllTicketsByEventId(long eventId) {
        ticketRepository.deleteByEventEventId(eventId);
        return HttpStatus.OK;
    }
}
