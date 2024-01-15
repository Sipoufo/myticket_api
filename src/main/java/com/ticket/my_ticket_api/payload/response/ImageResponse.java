package com.ticket.my_ticket_api.payload.response;

import com.ticket.my_ticket_api.entity.Image;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ImageResponse {
    private String uuid;
    private String name;
    private String type;
    private long size;

    public ImageResponse(Image image) {
        setUuid(image.getUuid());
        setName(image.getName());
        setType(image.getType());
        setSize(image.getSize());
    }
}
