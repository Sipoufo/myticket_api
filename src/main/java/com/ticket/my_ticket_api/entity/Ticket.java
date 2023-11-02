package com.ticket.my_ticket_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false)
    private Long ticket_id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private int number_place;
    @Column(nullable = false)
    private boolean visibility;
    @Column(nullable = false)
    private int price;

    @Temporal(TemporalType.TIMESTAMP)
    private Date create_at = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date update_at = new Date();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "event_event_id", nullable = false)
    private Event event;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "Ticket_users",
            joinColumns = @JoinColumn(name = "ticket_ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "user_user_id"))
    private List<Users> users = new ArrayList<>();

    public void addUser(Users user) {
        this.users.add(user);
        user.getTickets().add(this);
    }

    public void removeUser(long user_id) {
        Users user = this.users.stream().filter(t -> t.getUserId() == user_id).findFirst().orElse(null);
        if (user != null) {
            System.out.println(this.users.get(0).getFirstName());
            this.getUsers().remove(user);
            user.getTickets().remove(this);
        }
    }

}
