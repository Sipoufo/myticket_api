package com.ticket.my_ticket_api.service.EventService;

import com.ticket.my_ticket_api.entity.Category;
import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.payload.response.DataResponse;
import com.ticket.my_ticket_api.payload.response.MessageResponse;
import com.ticket.my_ticket_api.repository.EventRepository;
import com.ticket.my_ticket_api.service.categoryService.CategoryService;
import com.ticket.my_ticket_api.service.categoryService.CategoryServiceImpl;
import com.ticket.my_ticket_api.service.userService.UserService;
import com.ticket.my_ticket_api.service.userService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private final UserService userService = new UserServiceImpl();
    @Autowired
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    public ResponseEntity<?> createEvent(Event event, String token, long categoryId) {
        if (event.getName().length() < 3) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The name of event is small"));
        }
        Optional<Users> organizer = userService.getUserByToken(token);
        if (organizer.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User don't exist!"));
        }
        Optional<Category> category = categoryService.getOneCategory(categoryId);
        if (category.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This category don't exist!"));
        }
        event.setOrganizer(organizer.get());
        event.setCategory(category.get());
        event.setCreate_at(new Date());
        event.setUpdate_at(new Date());
        eventRepository.save(event);

        return ResponseEntity.ok(new MessageResponse("Event create Successfully"));
    }

    @Override
    public ResponseEntity<?> getAllEvents(Pageable pageable, String token) {
        Optional<Users> organizer = userService.getUserByToken(token);
        return organizer.map(users -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findByOrganizerUserIdIsNot(users.getUserId(), pageable).size())
                .data(eventRepository.findByOrganizerUserIdIsNot(users.getUserId(), pageable))
                .build())).orElseGet(() -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findAll(pageable).getContent().size())
                .data(eventRepository.findAll(pageable).getContent())
                .build()));
    }

    @Override
    public ResponseEntity<?> getAllMyEventsByIsPublish(Pageable pageable, String token, boolean isPublished) {
        Optional<Users> organizer = userService.getUserByToken(token);
        return organizer.map(users -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findByIsPublishedAndOrganizerUserId(isPublished, users.getUserId(), pageable).size())
                .data(eventRepository.findByIsPublishedAndOrganizerUserId(isPublished, users.getUserId(), pageable))
                .build())).orElseGet(() -> ResponseEntity.ok(DataResponse
                .builder()
                .build()));
    }

    @Override
    public ResponseEntity<?> getAllMyEventsByEndDateLessThanAndIsPublish(Pageable pageable, String token, boolean isPublished) {
        Optional<Users> organizer = userService.getUserByToken(token);
        return organizer.map(users -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findByEndEventLessThanAndIsPublishedAndOrganizerUserId(new Date(), isPublished, users.getUserId(), pageable).size())
                .data(eventRepository.findByEndEventLessThanAndIsPublishedAndOrganizerUserId(new Date(), isPublished, users.getUserId(), pageable))
                .build())).orElseGet(() -> ResponseEntity.ok(DataResponse
                .builder()
                .build()));
    }

    @Override
    public ResponseEntity<?> getAllMyEventsByEndDateGreaterThanAndIsPublish(Pageable pageable, String token, boolean isPublished) {
        Optional<Users> organizer = userService.getUserByToken(token);
        return organizer.map(users -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findByStartEventGreaterThanAndIsPublishedAndOrganizerUserId(new Date(), isPublished, users.getUserId(), pageable).size())
                .data(eventRepository.findByStartEventGreaterThanAndIsPublishedAndOrganizerUserId(new Date(), isPublished, users.getUserId(), pageable))
                .build())).orElseGet(() -> ResponseEntity.ok(DataResponse
                .builder()
                .build()));
    }

    @Override
    public ResponseEntity<?> getAllEventsByToken(Pageable pageable, String token) {
        Optional<Users> organizer = userService.getUserByToken(token);
        return organizer.map(users -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findByOrganizerUserId(users.getUserId(), pageable).size())
                .data(eventRepository.findByOrganizerUserId(users.getUserId(), pageable))
                .build())).orElseGet(() -> ResponseEntity.ok(DataResponse
                .builder()
                .build()));
    }

    @Override
    public ResponseEntity<?> getOneEvent(long eventId) {
        return ResponseEntity.ok(eventRepository.findById(eventId));
    }

    @Override
    public ResponseEntity<?> updateEvent(Event event, long eventId, long categoryId) {
        Optional<Event> event1 = eventRepository.findById(eventId);

        if (event1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This event don't exist!"));
        }

        Optional<Category> category = categoryService.getOneCategory(categoryId);
        if (category.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This category don't exist!"));
        }

        event1.get().setName(event.getName());
        event1.get().setDescription(event.getDescription());
        event1.get().setStartEvent(event.getStartEvent());
        event1.get().setEndEvent(event.getEndEvent());
        event1.get().setLocation(event.getLocation());
        event1.get().setLink(event.getLink());
        event1.get().setSend_link(event.isSend_link());
        event1.get().setEvent_summary(event.getEvent_summary());
        event1.get().setAdditional_info(event.getAdditional_info());
        event1.get().setPicture(event.getPicture());
        event1.get().setEvent_website(event.getEvent_website());
        event1.get().setFacebook_link(event.getFacebook_link());
        event1.get().setTwitter_link(event.getTwitter_link());
        event1.get().setEvent_type(event.getEvent_type());
        event1.get().setPrivate_listing(event.isPrivate_listing());
        event1.get().setShow_attendee_count(event.isShow_attendee_count());
        event1.get().setPublished(event.isPublished());
        event1.get().setCategory(category.get());
        event1.get().setUpdate_at(new Date());

        eventRepository.save(event1.get());
        return ResponseEntity.ok(new MessageResponse("Event updated Successfully"));
    }

    @Override
    public ResponseEntity<?> deleteEvent(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This event don't exist"));
        }
        event.get().setDeleted(true);
        return ResponseEntity.ok(eventRepository.save(event.get()));
    }

    @Override
    public List<Event> getAllEventsByOrganizerId(Pageable pageable, long userId) {
        return eventRepository.findByOrganizerUserId(userId, pageable);
    }

    @Override
    public HttpStatus deleteAllEventByOrganizerId(long userId) {
        eventRepository.deleteByOrganizerUserId(userId);
        return HttpStatus.OK;
    }

    @Override
    public List<Event> getAllEventsByCategoryId(long categoryId) {
        return eventRepository.findByCategoryCategoryId(categoryId);
    }

    @Override
    public HttpStatus deleteAllEventByCategoryId(long categoryId) {
        eventRepository.deleteByCategoryCategoryId(categoryId);
        return HttpStatus.OK;
    }
}