package com.ticket.my_ticket_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id", nullable = false)
    private Long event_id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private long start_event;
    @Column(nullable = false)
    private long end_event;
    private String description;
    @Column(nullable = false)
    private String location;
    private String additional_info;
    private List<String> picture;
    private String event_website;
    private String facebook_link;
    private String twitter_link;
    @Column(nullable = false)
    private String event_type;
    private boolean private_listing;
    private boolean show_attendee_count;
    private boolean is_published;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    @JsonIgnore
    private Users organizer;
}
