package com.ticket.my_ticket_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private long roleId;
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private ERole name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date create_at = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date update_at = new Date();
}
