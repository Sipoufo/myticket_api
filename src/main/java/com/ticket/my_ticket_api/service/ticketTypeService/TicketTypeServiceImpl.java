package com.ticket.my_ticket_api.service.ticketTypeService;

import com.ticket.my_ticket_api.entity.Ticket;
import com.ticket.my_ticket_api.entity.TicketType;
import com.ticket.my_ticket_api.payload.response.MessageResponse;
import com.ticket.my_ticket_api.repository.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketTypeServiceImpl implements TicketTypeService{
    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Override
    public ResponseEntity<?> createTicketType(TicketType ticketType) {
        Optional<TicketType> ticketType1 = ticketTypeRepository.findByName(ticketType.getName().toLowerCase());
        if (ticketType1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This type of ticket already exist !"));
        }
        ticketType.setName(ticketType.getName().toLowerCase());
        ticketTypeRepository.save(ticketType);
        return ResponseEntity
                .ok(new MessageResponse(ticketType.getName() + " ticket created"));
    }

    @Override
    public ResponseEntity<?> getAllTicketsType() {
        return ResponseEntity
                .ok(ticketTypeRepository.findAll());
    }

    @Override
    public ResponseEntity<?> getOneTicketType(long ticketTypeId) {
        return ResponseEntity
                .ok(ticketTypeRepository.findById(ticketTypeId));
    }

    @Override
    public ResponseEntity<?> updateSTicketType(TicketType ticketType, long ticketTypeId) {
        Optional<TicketType> ticketType1 = ticketTypeRepository.findById(ticketTypeId);
        if (ticketType1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This type of ticket not found !"));
        }
        ticketType1.get().setName(ticketType.getName());
        ticketTypeRepository.save(ticketType1.get());
        return ResponseEntity
                .ok(new MessageResponse("Type updated"));
    }

    @Override
    public ResponseEntity<?> deleteTicketType(long ticketTypeId) {
        Optional<TicketType> ticketType1 = ticketTypeRepository.findById(ticketTypeId);
        if (ticketType1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This type of ticket not found !"));
        }

        ticketTypeRepository.delete(ticketType1.get());
        return ResponseEntity
                .ok(new MessageResponse("Type deleted"));
    }
}
