package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Event;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Search
    List<Event> findByNameContainingAndDescriptionContaining(String query, String query2, Pageable pageable);
    List<Event> findByNameContainingAndDescriptionContainingAndOrganizerUserIdIsNot(String query, String query2, Long organizer_id, Pageable pageable);

    /* Category */
    List<Event> findByCategoryCategoryId(Long category_id, Pageable pageable);
    List<Event> findByCategoryCategoryIdAndOrganizerUserIdIsNot(Long category_id, Long organizer_id, Pageable pageable);
    @Transactional
    void deleteByCategoryCategoryId(Long category_id);

    /* Organizer */
    List<Event> findByOrganizerUserId(Long organizer_id, Pageable pageable);
    @Transactional
    void deleteByOrganizerUserId(Long organizer_id);

    // Others
    List<Event> findByOrganizerUserIdIsNot(Long organizer_id, Pageable pageable);
    List<Event> findByIsPublishedAndOrganizerUserId(boolean isPublished, Long organized_id, Pageable pageable);
    List<Event> findByEndEventLessThanAndIsPublishedAndOrganizerUserId(Date actualDate, boolean isPublished, Long organized_id, Pageable pageable);
    List<Event> findByStartEventGreaterThanAndIsPublishedAndOrganizerUserId(Date actualDate, boolean isPublished, Long organized_id, Pageable pageable);
}
