package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.RequestOrganiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestOrganiserRepository extends JpaRepository<RequestOrganiser, Long> {
}
