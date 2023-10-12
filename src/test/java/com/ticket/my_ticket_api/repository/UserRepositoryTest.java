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
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        this.ticket = Ticket
                .builder()
                .name("Full access")
                .description("ticket 1 description")
                .number_place(10)
                .visibility(false)
                .users(new ArrayList<>())
                .build();
        ticketRepository.save(ticket);
    }

    @Test
    public void should_find_no_user() {
        List<Users> users = userRepository.findAll();
        assertEquals(0, users.size());
    }

    @Test
    public void should_find_all_users () {
        Users user = Users
                .builder()
                .first_name("BLACKCode")
                .last_name("Yvan")
                .email("blackCode@gmail.com")
                .password("BlackCodeY$57")
                .phone("+237695914926")
                .tickets(new ArrayList<>())
                .build();
        userRepository.save(user);

        Users user_2 = Users
                .builder()
                .first_name("spring")
                .last_name("boot")
                .email("springboot@gmail.com")
                .password("SpringBoot$82")
                .phone("+237695975167")
                .tickets(new ArrayList<>())
                .build();
        userRepository.save(user_2);

        List<Users> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    public void should_create_user() {
        Users user = Users
                .builder()
                .first_name("BLACKCode")
                .last_name("Yvan")
                .email("blackCode@gmail.com")
                .password("BlackCodeY$57")
                .phone("+237695914926")
                .tickets(new ArrayList<>())
                .build();
        userRepository.save(user);
        assertNotNull(userRepository.save(user));
    }

    @Test
    public void should_find_user_by_id () {
        Users user = Users
                .builder()
                .first_name("BLACKCode")
                .last_name("Yvan")
                .email("blackCode@gmail.com")
                .password("BlackCodeY$57")
                .phone("+237695914926")
                .tickets(new ArrayList<>())
                .build();
        userRepository.save(user);
        assertEquals(userRepository.findById(user.getUserId()).get(), user);
    }

    @Test
    public void should_update_user_by_id() {
        Users user = Users
                .builder()
                .first_name("BLACKCode")
                .last_name("Yvan")
                .email("blackCode@gmail.com")
                .password("BlackCodeY$57")
                .phone("+237695914926")
                .tickets(new ArrayList<>())
                .build();
        userRepository.save(user);

        Users userFind = userRepository.findById(user.getUserId()).get();
        assertEquals(userFind, user);

        userFind.setFirst_name("CodePlus");
        userRepository.save(userFind);

        assertEquals(1, userRepository.findAll().size());
        Users checkUser = userRepository.findById(user.getUserId()).get();
        assertEquals(checkUser.getFirst_name(), "CodePlus");
    }

    @Test
    public void should_delete_user() {
        Users user = Users
                .builder()
                .first_name("BLACKCode")
                .last_name("Yvan")
                .email("blackCode@gmail.com")
                .password("BlackCodeY$57")
                .phone("+237695914926")
                .tickets(new ArrayList<>())
                .build();
        userRepository.save(user);

        assertEquals(1, userRepository.findAll().size());

        userRepository.deleteById(user.getUserId());

        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    public void should_add_ticket() {
        Users user = Users
                .builder()
                .first_name("BLACKCode")
                .last_name("Yvan")
                .email("blackCode@gmail.com")
                .password("BlackCodeY$57")
                .phone("+237695914926")
                .tickets(new ArrayList<>())
                .build();
        userRepository.save(user);

        assertEquals(1, userRepository.findAll().size());
        assertEquals(0, userRepository.findById(user.getUserId()).get().getTickets().size());

        Users userFind = userRepository.findById(user.getUserId()).get();
        userFind.addTicket(this.ticket);

        assertEquals(1, userRepository.findById(user.getUserId()).get().getTickets().size());
    }

    @Test
    public void should_remove_user() {
        Users user = Users
                .builder()
                .first_name("BLACKCode")
                .last_name("Yvan")
                .email("blackCode@gmail.com")
                .password("BlackCodeY$57")
                .phone("+237695914926")
                .tickets(new ArrayList<>())
                .build();
        user.addTicket(this.ticket);
        userRepository.save(user);

        assertEquals(1, userRepository.findAll().size());
        assertEquals(1, userRepository.findById(user.getUserId()).get().getTickets().size());

        Users userFind = userRepository.findById(user.getUserId()).get();
        userFind.removeTicket(this.ticket.getTicket_id());

        assertEquals(1, userRepository.findAll().size());
        assertEquals(0, userRepository.findById(user.getUserId()).get().getTickets().size());
    }
}