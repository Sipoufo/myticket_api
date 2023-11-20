package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.EventLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
}
