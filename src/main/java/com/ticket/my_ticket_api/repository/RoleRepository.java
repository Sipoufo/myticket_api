package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.ERole;
import com.ticket.my_ticket_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
