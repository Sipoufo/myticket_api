package com.ticket.my_ticket_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String first_name;
    @Column(nullable = false)
    private String last_name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String phone;

    private String website;
    private String Company;
    private boolean is_organizer;
    private String position;
    private String address;
    private String address2;
    private String city;
    private String country;
    private String code_postal;
    private String region;
    private String picture;
    private String restart_password_token;

    @ManyToMany(mappedBy = "users", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Ticket> tickets = new ArrayList<>();

    public void addTicket(Ticket ticket) {
        Ticket ticketFind = this.tickets.stream().filter(t -> Objects.equals(t.getTicket_id(), ticket.getTicket_id())).findFirst().orElse(null);
        if (ticketFind == null) {
            this.tickets.add(ticket);
            ticket.getUsers().add(this);
        }
    }

    public void removeTicket(long ticket_id) {
        Ticket ticket = this.tickets.stream().filter(t -> t.getTicket_id() == ticket_id).findFirst().orElse(null);
        if (ticket != null) {
            this.getTickets().remove(ticket);
            ticket.getUsers().remove(this);
        }
    }
}
