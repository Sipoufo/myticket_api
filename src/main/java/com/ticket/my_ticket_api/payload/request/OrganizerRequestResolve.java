package com.ticket.my_ticket_api.payload.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrganizerRequestResolve {
    private boolean isAccepted;
    private String message;
}
