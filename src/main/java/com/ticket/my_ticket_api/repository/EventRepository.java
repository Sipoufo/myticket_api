package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Event;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    /* Category */
    List<Event> findByCategoryCategoryId(Long category_id);
    @Transactional
    void deleteByCategoryCategoryId(Long category_id);

    /* Organizer */
    List<Event> findByOrganizerUserId(Long organizer_id);
    @Transactional
    void deleteByOrganizerUserId(Long organizer_id);
}
