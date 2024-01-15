package com.ticket.my_ticket_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "request_organiser")
public class RequestOrganiser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_organiser_id", nullable = false)
    private Long requestOrganiserId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cni_face_id", nullable = false)
    @JsonIgnore
    private Image CNIFace;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cni_back_id", nullable = false)
    @JsonIgnore
    private Image CNIBack;

    private String CNIFaceName;
    private String CNIBackName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private boolean isAccepted;
    private String message;

    @CreatedDate
    protected Date create_at;

    @LastModifiedDate
    protected Date update_at;
}
