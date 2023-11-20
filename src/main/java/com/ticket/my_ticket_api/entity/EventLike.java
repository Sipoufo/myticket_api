package com.ticket.my_ticket_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "ticket_like")
public class EventLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_like_id", nullable = false)
    private Long eventLikeId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
