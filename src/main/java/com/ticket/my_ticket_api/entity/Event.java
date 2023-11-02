package com.ticket.my_ticket_api.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startEvent;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endEvent;
    private String description;
    private String location;
    @Column(nullable = true)
    private String link;
    private boolean send_link = false;
    @Column(nullable = true)
    private String event_summary;
    private String additional_info;
    private List<String> picture;
    private String event_website;
    private String facebook_link;
    private String twitter_link;
    @Column(nullable = false)
    private String event_type;
    private boolean private_listing;
    private boolean show_attendee_count;
    private boolean isPublished = false;
    private boolean isDeleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date create_at = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date update_at = new Date();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizer_id", nullable = false)
    private Users organizer;
}
