package com.ticket.my_ticket_api.payload.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MissingEventResponse {
    private String message;
    private List<String> missingFields;
}
