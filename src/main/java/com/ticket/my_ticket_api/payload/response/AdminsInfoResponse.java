package com.ticket.my_ticket_api.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AdminsInfoResponse {
    private int adminNumber;
    private int adminActiveNumber;
    private int adminBlockNumber;
}
