package com.ticket.my_ticket_api.payload.request;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventRequest {
    private String name;
    private String startEvent;
    private String endEvent;
    private String location;
    private String eventType;
    private long categoryId;
}
