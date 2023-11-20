package com.ticket.my_ticket_api.payload.request;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TicketRequest {
    private String name;
    private String description;
    private int number_place;
    private boolean visibility;
    private int price;
    private long eventId;
    private long ticketTypeId;
}
