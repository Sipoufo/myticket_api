package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.RequestOrganiser;
import com.ticket.my_ticket_api.helpers.FileNameHelper;
import com.ticket.my_ticket_api.payload.request.EventRequest;
import com.ticket.my_ticket_api.payload.request.OrganizerRequest;
import com.ticket.my_ticket_api.service.requestOrganiser.RequestOrganiserService;
import com.ticket.my_ticket_api.service.requestOrganiser.RequestOrganiserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class RequestOrganiserController {
    @Autowired
    private RequestOrganiserService requestOrganiserService = new RequestOrganiserServiceImpl();
    private FileNameHelper fileHelper = new FileNameHelper();

    @PostMapping("/requestOrganizer")
    public ResponseEntity<?> createRequest(@RequestParam("cni_face") MultipartFile cni_face, @RequestParam("cni_back") MultipartFile cni_back, @RequestHeader (name="Authorization") String token) {
        OrganizerRequest organizerRequest = OrganizerRequest
                .builder()
                .cni_face(cni_face)
                .cni_back(cni_back)
                .build();

        return requestOrganiserService.save(organizerRequest, token, fileHelper);
    }

    @GetMapping("/requestOrganizer/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getAllRequest(@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize, @RequestHeader (name="Authorization") String token) {
        System.out.println("Je passe");

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        List<RequestOrganiser> requestOrganisers = requestOrganiserService.fetchAllRequestOrganiser(pageable);
        return ResponseEntity.ok()
                .body(requestOrganisers);
    }
}
