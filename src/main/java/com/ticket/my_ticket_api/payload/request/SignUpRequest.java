package com.ticket.my_ticket_api.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SignUpRequest {
    private String email;
    private String role;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
}
