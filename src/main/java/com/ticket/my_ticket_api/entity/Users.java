package com.ticket.my_ticket_api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String phone;
    private String username;

    private String website;
    private String Company;
    private boolean is_organizer;
    private String position;
    private String address;
    private String address2;
    private String city;
    private String country;
    @Column(nullable = true)
    private String country_code;
    private String code_postal;
    @Column(nullable = true)
    private String state;
    @Column(nullable = true)
    private String state_code;
    private String picture;
    private String restart_password_token;
    @Column(nullable = true)
    private boolean isValidated = false;
    @Column(nullable = true)
    private String token_validation;
    @Column(nullable = true)
    private boolean isDeleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date create_at = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date update_at = new Date();

    /* @ManyToMany(mappedBy = "users")
    private List<Ticket> tickets = new ArrayList<>();

    public void addTicket(Ticket ticket) {
        Ticket ticketFind = this.tickets.stream().filter(t -> Objects.equals(t.getTicketId(), ticket.getTicketId())).findFirst().orElse(null);
        if (ticketFind == null) {
            this.tickets.add(ticket);
            ticket.getUsers().add(this);
        }
    }

    public void removeTicket(long ticket_id) {
        Ticket ticket = this.tickets.stream().filter(t -> t.getTicketId() == ticket_id).findFirst().orElse(null);
        if (ticket != null) {
            this.getTickets().remove(ticket);
            ticket.getUsers().remove(this);
        }
    } */

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roles_role_id")
    private Role role;
}
