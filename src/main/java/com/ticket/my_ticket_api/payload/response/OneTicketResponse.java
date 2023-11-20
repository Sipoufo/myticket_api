package com.ticket.my_ticket_api.payload.response;

import com.ticket.my_ticket_api.entity.Ticket;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OneTicketResponse {
    private int ticketNumber;
    private Ticket ticket;
}
