package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.payload.request.EventDetailRequest;
import com.ticket.my_ticket_api.payload.request.EventRequest;
import com.ticket.my_ticket_api.service.EventService.EventService;
import com.ticket.my_ticket_api.service.EventService.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class EventController {
    @Autowired
    private final EventService eventService = new EventServiceImpl();

    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@Validated @RequestBody EventRequest eventRequest, @RequestHeader (name="Authorization") String token) throws ParseException {
        /*System.out.println("token " + new Date());*/

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Event event = Event
                .builder()
                .name(eventRequest.getName())
                .startEvent(formatter.parse(eventRequest.getStartEvent()))
                .endEvent(formatter.parse(eventRequest.getEndEvent()))
                .location(eventRequest.getLocation())
                .event_type(eventRequest.getEventType())
                .build();

        return eventService.createEvent(event, token, eventRequest.getCategoryId());
    }

    @GetMapping("/event/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getEventByToken(@RequestHeader (name="Authorization") String token, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return eventService.getAllEventsByToken(pageable, token);
    }

    @GetMapping("/event/isPublish/{isPublish}/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getAllMyEventsByIsPublish(@RequestHeader (name="Authorization") String token, @PathVariable("isPublish") boolean isPublish, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        System.out.println(isPublish);
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return eventService.getAllMyEventsByIsPublish(pageable, token, isPublish);
    }

    @GetMapping("/event/lessThantToday/{isPublish}/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getAllMyEventsByEndDateLessThanAndIsPublish(@RequestHeader (name="Authorization") String token, @PathVariable("isPublish") boolean isPublish, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return eventService.getAllMyEventsByEndDateLessThanAndIsPublish(pageable, token, isPublish);
    }

    @GetMapping("/event/greaterThantToday/{isPublish}/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getAllMyEventsByEndDateGreaterThanAndIsPublish(@RequestHeader (name="Authorization") String token, @PathVariable("isPublish") boolean isPublish, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return eventService.getAllMyEventsByEndDateGreaterThanAndIsPublish(pageable, token, isPublish);
    }

    @GetMapping("/permit/event/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getEvents(@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize, @RequestHeader (name="Authorization") String token) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return eventService.getAllEvents(pageable, token);
    }

    @GetMapping("/permit/event/{categoryId}/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getEventsByCategory(@PathVariable("categoryId") long categoryId, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize, @RequestHeader (name="Authorization") String token) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return eventService.getAllEventsByCategoryId(pageable, categoryId, token);
    }

    @GetMapping("/permit/event/search/{search}/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getSearchEvent(@PathVariable("search") String search, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize, @RequestHeader (name="Authorization") String token) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return eventService.getSearchEvents(pageable, search, token);
    }

    @GetMapping("/permit/event/{id}")
    public ResponseEntity<?> getOneEvent(@PathVariable("id") long id) {
        return eventService.getOneEvent(id);
    }

    @PutMapping("/event")
    public ResponseEntity<?> updateEvent(@Validated @RequestBody EventDetailRequest eventDetailRequest) throws ParseException {
        System.out.println("1111111");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        System.out.println(formatter);
        Event event = Event
                .builder()
                .name(eventDetailRequest.getName())
                .startEvent(formatter.parse(eventDetailRequest.getStartEvent()))
                .endEvent(formatter.parse(eventDetailRequest.getEndEvent()))
                .event_type(eventDetailRequest.getEventType())
                .link(eventDetailRequest.getLink())
                .additional_info(eventDetailRequest.getAdditional_info())
                .send_link(eventDetailRequest.isSend_link())
                .event_summary(eventDetailRequest.getEvent_summary())
                .description(eventDetailRequest.getDescription())
                .event_type(eventDetailRequest.getEventType())
                .event_website(eventDetailRequest.getEvent_website())
                .facebook_link(eventDetailRequest.getFacebook_link())
                .twitter_link(eventDetailRequest.getTwitter_link())
                .build();
        return eventService.updateEvent(event, eventDetailRequest.getEventId(), eventDetailRequest.getCategoryId());
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<?> deleteOneEvent(@PathVariable("id") long id) {
        return eventService.deleteEvent(id);
    }

    @GetMapping("/event/publish/{id}/{publish}")
    public ResponseEntity<?> publishOneEvent(@PathVariable("id") long id, @PathVariable("publish") boolean publish) {
        System.out.println("isPublish" + publish);
        if (publish) {
            return eventService.publishEvent(id);
        }
        return eventService.unPublishEvent(id);
    }
}
