package com.ticket.my_ticket_api.service.requestOrganiser;

import com.ticket.my_ticket_api.entity.RequestOrganiser;
import com.ticket.my_ticket_api.helpers.FileNameHelper;
import com.ticket.my_ticket_api.payload.request.OrganizerRequest;
import com.ticket.my_ticket_api.payload.request.OrganizerRequestResolve;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RequestOrganiserService {
    public ResponseEntity<?> save(OrganizerRequest organizerRequest, String token, FileNameHelper helper);
    public ResponseEntity<List<RequestOrganiser>> fetchAllRequestOrganiser(Pageable pageable);
    public ResponseEntity<?> resolveRequest(OrganizerRequestResolve organizerRequestResolve, Long requestId);
}
