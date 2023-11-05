package com.ticket.my_ticket_api.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TokenVerifyResponse {
    private boolean isValid;
    private String message;
}
