package com.ticket.my_ticket_api.service.EventService;

import com.ticket.my_ticket_api.entity.Category;
import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.payload.response.DataResponse;
import com.ticket.my_ticket_api.payload.response.MessageResponse;
import com.ticket.my_ticket_api.payload.response.MissingEventResponse;
import com.ticket.my_ticket_api.repository.EventRepository;
import com.ticket.my_ticket_api.repository.TicketBuyRepository;
import com.ticket.my_ticket_api.service.categoryService.CategoryService;
import com.ticket.my_ticket_api.service.categoryService.CategoryServiceImpl;
import com.ticket.my_ticket_api.service.userService.UserService;
import com.ticket.my_ticket_api.service.userService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private TicketBuyRepository ticketBuyRepository;
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
                .dataNumber(eventRepository.findByOrganizerUserIdIsNotAndIsDeletedFalse(users.getUserId(), pageable).size())
                .data(eventRepository.findByOrganizerUserIdIsNotAndIsDeletedFalse(users.getUserId(), pageable))
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
                .dataNumber(eventRepository.findByIsPublishedAndOrganizerUserIdAndIsDeletedFalse(isPublished, users.getUserId(), pageable).size())
                .data(eventRepository.findByIsPublishedAndOrganizerUserIdAndIsDeletedFalse(isPublished, users.getUserId(), pageable))
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
                .dataNumber(eventRepository.findByEndEventLessThanAndIsPublishedAndOrganizerUserIdAndIsDeletedFalse(new Date(), isPublished, users.getUserId(), pageable).size())
                .data(eventRepository.findByEndEventLessThanAndIsPublishedAndOrganizerUserIdAndIsDeletedFalse(new Date(), isPublished, users.getUserId(), pageable))
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
                .dataNumber(eventRepository.findByStartEventGreaterThanAndIsPublishedAndOrganizerUserIdAndIsDeletedFalse(new Date(), isPublished, users.getUserId(), pageable).size())
                .data(eventRepository.findByStartEventGreaterThanAndIsPublishedAndOrganizerUserIdAndIsDeletedFalse(new Date(), isPublished, users.getUserId(), pageable))
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
                .dataNumber(eventRepository.findByOrganizerUserIdAndIsDeletedFalse(users.getUserId(), pageable).size())
                .data(eventRepository.findByOrganizerUserIdAndIsDeletedFalse(users.getUserId(), pageable))
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
        return eventRepository.findByOrganizerUserIdAndIsDeletedFalse(userId, pageable);
    }

    @Override
    public HttpStatus deleteAllEventByOrganizerId(long userId) {
        eventRepository.deleteByOrganizerUserId(userId);
        return HttpStatus.OK;
    }

    @Override
    public ResponseEntity<?> getAllEventsByCategoryId(Pageable pageable, long categoryId, String token) {
        Optional<Users> organizer = userService.getUserByToken(token);
        return organizer.map(users -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findByCategoryCategoryIdAndOrganizerUserIdIsNot(categoryId, users.getUserId(), pageable).size())
                .data(eventRepository.findByCategoryCategoryIdAndOrganizerUserIdIsNot(categoryId, users.getUserId(), pageable))
                .build())).orElseGet(() -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findByCategoryCategoryId(categoryId, pageable).size())
                .data(eventRepository.findByCategoryCategoryId(categoryId, pageable))
                .build()));
    }

    @Override
    public ResponseEntity<?> getSearchEvents(Pageable pageable, String search, String token) {
        Optional<Users> organizer = userService.getUserByToken(token);
        return organizer.map(users -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findByNameContainingAndDescriptionContainingAndOrganizerUserIdIsNot(search, search, users.getUserId(), pageable).size())
                .data(eventRepository.findByNameContainingAndDescriptionContainingAndOrganizerUserIdIsNot(search, search, users.getUserId(), pageable))
                .build())).orElseGet(() -> ResponseEntity.ok(DataResponse
                .builder()
                .actualPage(pageable.getPageNumber() + 1)
                .dataNumber(eventRepository.findByNameContainingAndDescriptionContaining(search, search, pageable).size())
                .data(eventRepository.findByNameContainingAndDescriptionContaining(search, search, pageable))
                .build()));
    }

    @Override
    public HttpStatus deleteAllEventByCategoryId(long categoryId) {
        eventRepository.deleteByCategoryCategoryId(categoryId);
        return HttpStatus.OK;
    }

    @Override
    public ResponseEntity<?> publishEvent(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        List<String> missingElement = new ArrayList<>();

        if (event.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This event don't exist!"));
        }

        if (event.get().getName() == null) {
            System.out.println("1");
            missingElement.add("Name missing");
        }
        if (event.get().getDescription() == null) {
            System.out.println("2");
            missingElement.add("Description missing");
        }
        if (event.get().getStartEvent() == null) {
            System.out.println("3");
            missingElement.add("Start Event missing");
        }
        if (event.get().getEndEvent() == null) {
            System.out.println("4");
            missingElement.add("End Event missing");
        }
        if (event.get().getEvent_summary() == null) {
            System.out.println("5");
            missingElement.add("Event summary missing");
        }
        if (event.get().getEvent_type() == null) {
            System.out.println("6");
            missingElement.add("Name missing");
        }
        if (event.get().getCategory() == null) {
            System.out.println("7");
            missingElement.add("Category missing");
        }
        if (event.get().getOrganizer() == null) {
            System.out.println("8");
            missingElement.add("Organizer missing");
        }

        if (!missingElement.isEmpty()) {
            System.out.println(missingElement.size());
            return ResponseEntity
                    .badRequest()
                    .body(MissingEventResponse
                            .builder()
                            .message("You can't publish this event because you have missing fields")
                            .missingFields(missingElement)
                            .build());
        }
        event.get().setPublished(true);
        eventRepository.save(event.get());
        return ResponseEntity.ok("Your event is successful created");
    }

    @Override
    public ResponseEntity<?> unPublishEvent(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This event don't exist!"));
        }
        
        event.get().setPublished(false);
        eventRepository.save(event.get());
        return ResponseEntity.ok("Your event is successful unpublished");
    }

    @Override
    public ResponseEntity<?> getAllByUserId_admin(long organizerId, Pageable pageable) {
        return ResponseEntity.ok(DataResponse
                .builder()
                .data(eventRepository.findByOrganizerUserIdAndIsDeletedFalse(organizerId, pageable))
                .dataNumber(eventRepository.findByOrganizerUserIdAndIsDeletedFalse(organizerId, pageable).size())
                .actualPage(pageable.getPageNumber() + 1)
                .pageable(pageable)
                .build()
        );
    }
}
