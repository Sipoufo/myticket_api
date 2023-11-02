package com.ticket.my_ticket_api.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventDetailRequest {
    private long eventId;
    private String link;
    private String additional_info;
    private boolean send_link;
    private String name;
    private String event_summary;
    private long categoryId;
    private String eventType;
    private String startEvent;
    private String endEvent;
    private String description;
    private String event_website;
    private String facebook_link;
    private String twitter_link;
}
