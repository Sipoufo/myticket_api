package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.TicketBuy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketBuyRepository extends JpaRepository<TicketBuy, Long> {
    @Query(value = "SELECT DISTINCT ON (ticket_id) ticket_id, ticket_buy_id, user_id FROM ticket_buy WHERE user_id = :userId", nativeQuery = true)
    List<TicketBuy> findByUserUserId(@Param("userId") Long userId, Pageable pageable);
    List<TicketBuy> findByTicketTicketId(Long ticketId, Pageable pageable);
    List<TicketBuy> findByTicketTicketIdAndUserUserId(long ticketId, long userId);
    List<TicketBuy> findByTicketTicketIdAndUserUserId(Long ticketId, Long userId, Pageable pageable);
}