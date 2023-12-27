package com.ticket.my_ticket_api.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserCrucialInfo {
    private String email;
    private String password;
}
