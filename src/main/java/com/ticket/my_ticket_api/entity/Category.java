package com.ticket.my_ticket_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String picture;

    @Temporal(TemporalType.TIMESTAMP)
    private Date create_at = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date update_at = new Date();
}
