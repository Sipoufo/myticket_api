package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Category;
import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Users user;
    private Category category;

    @BeforeEach
    void setUp() {
        this.user = Users
                .builder()
                .firstName("BLACKCode")
                .lastName("Yvan")
                .email("blackCode@gmail.com")
                .password("BlackCodeY$57")
                .phone("+237695914926")
                .tickets(new ArrayList<>())
                .build();
        userRepository.save(this.user);

        this.category = Category
                .builder()
                .name("Programming")
                .build();
    }

    @Test
    public void should_find_no_Event() {
        List<Event> events = eventRepository.findAll();
        assertEquals(0, events.size());
    }

    @Test
    public void should_find_all_events() {
        Event event = Event
                .builder()
                .name("Billiard part")
                .description("Billiard part")
                .startEvent(new Date())
                .endEvent(new Date())
                .location("Yassa")
                .event_type("online")
                .category(this.category)
                .organizer(this.user)
                .build();
        eventRepository.save(event);

        List<Event> events = eventRepository.findAll();
        assertEquals(1, events.size());
    }

    @Test
    public void should_create_event () {
        Event event = Event
                .builder()
                .name("Billiard part")
                .description("Billiard part")
                .startEvent(new Date())
                .endEvent(new Date())
                .location("Yassa")
                .event_type("online")
                .category(this.category)
                .organizer(this.user)
                .build();
        eventRepository.save(event);

        assertNotNull(eventRepository.save(event));
    }

    @Test
    public void should_find_event_by_id () {
        Event event = Event
                .builder()
                .name("Billiard part")
                .description("Billiard part")
                .startEvent(new Date())
                .endEvent(new Date())
                .location("Yassa")
                .event_type("online")
                .category(this.category)
                .organizer(this.user)
                .build();
        eventRepository.save(event);

        assertEquals(eventRepository.findById(event.getEventId()).get(), event);
    }

    @Test
    public void should_update_event_by_id() {
        Event event = Event
                .builder()
                .name("Billiard part")
                .description("Billiard part")
                .startEvent(new Date())
                .endEvent(new Date())
                .location("Yassa")
                .event_type("online")
                .category(this.category)
                .organizer(this.user)
                .build();
        eventRepository.save(event);

        Event eventFind = eventRepository.findById(event.getEventId()).get();
        assertEquals(eventFind, event);

        eventFind.setName("Programming part");
        eventRepository.save(eventFind);

        assertEquals(1, eventRepository.findAll().size());
        Event checkTicket = eventRepository.findById(event.getEventId()).get();
        assertEquals(checkTicket.getName(), "Programming part");
    }

    @Test
    public void should_delete_event() {
        Event event = Event
                .builder()
                .name("Billiard part")
                .description("Billiard part")
                .startEvent(new Date())
                .endEvent(new Date())
                .location("Yassa")
                .event_type("online")
                .category(this.category)
                .organizer(this.user)
                .build();
        eventRepository.save(event);

        assertEquals(1, eventRepository.findAll().size());

        eventRepository.deleteById(event.getEventId());

        assertEquals(0, eventRepository.findAll().size());
    }

    @Test
    public void should_find_events_by_category_id() {
        Event event = Event
                .builder()
                .name("Billiard part")
                .description("Billiard part")
                .startEvent(new Date())
                .endEvent(new Date())
                .location("Yassa")
                .event_type("online")
                .category(this.category)
                .organizer(this.user)
                .build();
        eventRepository.save(event);

        assertEquals(1, eventRepository.findAll().size());
        assertEquals(1, eventRepository.findByCategoryCategoryId(this.category.getCategoryId()).size());
    }

    @Test
    public void should_find_events_by_user_id() {
        Event event = Event
                .builder()
                .name("Billiard part")
                .description("Billiard part")
                .startEvent(new Date())
                .endEvent(new Date())
                .location("Yassa")
                .event_type("online")
                .category(this.category)
                .organizer(this.user)
                .build();
        eventRepository.save(event);

        assertEquals(1, eventRepository.findAll().size());
        assertEquals(1, eventRepository.findByCategoryCategoryId(this.user.getUserId()).size());
    }

    @Test
    public void should_delete_events_by_user_id() {
        Event event = Event
                .builder()
                .name("Billiard part")
                .description("Billiard part")
                .startEvent(new Date())
                .endEvent(new Date())
                .location("Yassa")
                .event_type("online")
                .category(this.category)
                .organizer(this.user)
                .build();
        eventRepository.save(event);

        assertEquals(1, eventRepository.findAll().size());
        eventRepository.deleteByOrganizerUserId(this.user.getUserId());
        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    public void should_delete_events_by_category_id() {
        Event event = Event
                .builder()
                .name("Billiard part")
                .description("Billiard part")
                .startEvent(new Date())
                .endEvent(new Date())
                .location("Yassa")
                .event_type("online")
                .category(this.category)
                .organizer(this.user)
                .build();
        eventRepository.save(event);

        assertEquals(1, eventRepository.findAll().size());
        eventRepository.deleteByCategoryCategoryId(this.category.getCategoryId());
        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, categoryRepository.findAll().size());
    }
}