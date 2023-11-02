package com.ticket.my_ticket_api.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserMachineDetails {
    private String browser;
    private String operatingSystem;
    private String ipAddress;
}
