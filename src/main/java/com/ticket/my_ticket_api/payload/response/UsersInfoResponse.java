package com.ticket.my_ticket_api.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UsersInfoResponse {
    private int userNumber;
    private int eventNumber;
    private int ticketNumber;
}
