package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Ticket;
import com.ticket.my_ticket_api.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TicketRepositoryTest {
    @Autowired
    private TicketRepository ticketRepository;

    private Users user;

    @BeforeEach
    void setUp() {
        this.user = Users
                .builder()
                .first_name("BLACKCode")
                .last_name("Yvan")
                .email("blackCode@gmail.com")
                .password("BlackCodeY$57")
                .phone("+237695914926")
                .tickets(new ArrayList<>())
                .build();
    }

    @Test
    public void should_find_no_ticket() {
        List<Ticket> categories = ticketRepository.findAll();
        assertEquals(0, categories.size());
    }

    @Test
    public void should_find_all_tickets () {
        Ticket ticket = Ticket
                .builder()
                .name("Full access")
                .description("ticket 1 description")
                .number_place(10)
                .visibility(false)
                .users(new ArrayList<>())
                .build();
        ticketRepository.save(ticket);

        Ticket ticket_2 = Ticket
                .builder()
                .name("Full access 2")
                .description("ticket 2 description")
                .number_place(10)
                .visibility(true)
                .users(new ArrayList<>())
                .build();
        ticketRepository.save(ticket_2);

        List<Ticket> tickets = ticketRepository.findAll();
        assertEquals(2, tickets.size());
    }

    @Test
    public void should_create_ticket () {
        Ticket ticket = Ticket
                .builder()
                .name("Full access")
                .description("ticket 1 description")
                .number_place(10)
                .visibility(false)
                .users(new ArrayList<>())
                .build();
        ticketRepository.save(ticket);
        assertNotNull(ticketRepository.save(ticket));
    }

    @Test
    public void should_find_category_by_id () {
        Ticket ticket = Ticket
                .builder()
                .name("Full access")
                .description("ticket 1 description")
                .number_place(10)
                .visibility(false)
                .users(new ArrayList<>())
                .build();
        ticketRepository.save(ticket);
        assertEquals(ticketRepository.findById(ticket.getTicket_id()).get(), ticket);
    }

    @Test
    public void should_update_category_by_id() {
        Ticket ticket = Ticket
                .builder()
                .name("Full access")
                .description("ticket 1 description")
                .number_place(10)
                .visibility(false)
                .users(new ArrayList<>())
                .build();
        ticketRepository.save(ticket);

        Ticket ticketFind = ticketRepository.findById(ticket.getTicket_id()).get();
        assertEquals(ticketFind, ticket);

        ticketFind.setName("restricted access");
        ticketRepository.save(ticketFind);

        assertEquals(1, ticketRepository.findAll().size());
        Ticket checkTicket = ticketRepository.findById(ticket.getTicket_id()).get();
        assertEquals(checkTicket.getName(), "restricted access");
    }

    @Test
    public void should_delete_category() {
        Ticket ticket = Ticket
                .builder()
                .name("Full access")
                .description("ticket 1 description")
                .number_place(10)
                .visibility(false)
                .users(new ArrayList<>())
                .build();
        ticketRepository.save(ticket);

        assertEquals(1, ticketRepository.findAll().size());

        ticketRepository.deleteById(ticket.getTicket_id());

        assertEquals(0, ticketRepository.findAll().size());
    }

    @Test
    public void should_add_user() {
        Ticket ticket = Ticket
                .builder()
                .name("Full access")
                .description("ticket 1 description")
                .number_place(10)
                .visibility(false)
                .users(new ArrayList<>())
                .build();
        ticketRepository.save(ticket);

        assertEquals(1, ticketRepository.findAll().size());
        assertEquals(0, ticketRepository.findById(ticket.getTicket_id()).get().getUsers().size());

        Ticket ticketFind = ticketRepository.findById(ticket.getTicket_id()).get();
        ticketFind.addUser(this.user);

        assertEquals(1, ticketRepository.findById(ticket.getTicket_id()).get().getUsers().size());
    }

    @Test
    public void should_remove_user() {
        Ticket ticket = Ticket
                .builder()
                .name("Full access")
                .description("ticket 1 description")
                .number_place(10)
                .visibility(false)
                .users(new ArrayList<>())
                .build();
        ticket.addUser(this.user);
        ticketRepository.save(ticket);

        assertEquals(1, ticketRepository.findAll().size());
        assertEquals(1, ticketRepository.findById(ticket.getTicket_id()).get().getUsers().size());

        Ticket ticketFind = ticketRepository.findById(ticket.getTicket_id()).get();
        ticketFind.removeUser(this.user.getUserId());

        assertEquals(1, ticketRepository.findAll().size());
        assertEquals(0, ticketRepository.findById(ticket.getTicket_id()).get().getUsers().size());
    }
}