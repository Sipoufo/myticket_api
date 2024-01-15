package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Image;
import com.ticket.my_ticket_api.payload.response.ImageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByName(String fileName);
}
