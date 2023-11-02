package com.ticket.my_ticket_api.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String firstName;

    public JwtResponse(String accessToken, String firstName) {
        this.token = accessToken;
        this.firstName = firstName;
    }
}
