package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByFirstName(String firstName);
    Optional<Users> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    /* Role */
    List<Users> findByRoleRoleId(Long role_id);
    @Transactional
    void deleteByRoleRoleId(Long role_id);
}
