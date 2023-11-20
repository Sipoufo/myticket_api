package com.ticket.my_ticket_api.payload.response;

import com.ticket.my_ticket_api.entity.Ticket;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DataResponse implements Serializable {
    private List<?> data;
    private int actualPage;
    private int dataNumber;
}
