package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.ERole;
import com.ticket.my_ticket_api.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    // Search
    List<Users> findByFirstNameContainingOrLastNameContainingOrEmailContainingAndRoleNameIsNotAndUserIdIsNot(String query, String query2, String query3, ERole name, long userId, Pageable pageable);
    List<Users> findByRoleNameIsNotAndUserIdIsNot(ERole role, long userId, Pageable pageable);
    List<Users> findByRoleNameIsNotAndUserIdIsNot(ERole role, long userId);
    Optional<Users> findByUserId(long userId);
    Optional<Users> findByFirstName(String firstName);
    Optional<Users> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    /* Role */
    List<Users> findByRoleRoleId(Long role_id);
    List<Users> findByRoleName(ERole role);
    List<Users> findByRoleNameAndIsDeleted(ERole role, boolean isDeleted);
    @Transactional
    void deleteByRoleRoleId(Long role_id);
}
