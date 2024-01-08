package com.ticket.my_ticket_api.payload.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrganizerRequest {
    private MultipartFile cni_face;
    private MultipartFile cni_back;
}
