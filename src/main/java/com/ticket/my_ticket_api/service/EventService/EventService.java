package com.ticket.my_ticket_api.service.EventService;

import com.ticket.my_ticket_api.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {
    public ResponseEntity<?> createEvent(Event event, String token, long categoryId);
    public ResponseEntity<?> getAllEvents(Pageable pageable, String token);
    public ResponseEntity<?> getAllEventsByToken(Pageable pageable, String token);
    public ResponseEntity<?> getAllMyEventsByIsPublish(Pageable pageable, String token, boolean isPublished);
    public ResponseEntity<?> getAllMyEventsByEndDateLessThanAndIsPublish(Pageable pageable, String token, boolean isPublished);
    public ResponseEntity<?> getAllMyEventsByEndDateGreaterThanAndIsPublish(Pageable pageable, String token, boolean isPublished);
    public ResponseEntity<?> getOneEvent(long eventId);
    public ResponseEntity<?> updateEvent(Event event, long eventId, long categoryId);
    public ResponseEntity<?> deleteEvent(long eventId);
    public List<Event> getAllEventsByOrganizerId(Pageable pageable, long userId);
    public HttpStatus deleteAllEventByOrganizerId(long userId);
    public List<Event> getAllEventsByCategoryId(long categoryId);
    public HttpStatus deleteAllEventByCategoryId(long categoryId);
}
