package com.ticket.my_ticket_api.payload.response;

import com.ticket.my_ticket_api.entity.Users;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserTicketBuyResponse {
    private int ticketCount;
    private Users user;
}
