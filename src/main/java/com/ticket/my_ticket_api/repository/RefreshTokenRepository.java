package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.RefreshToken;
import com.ticket.my_ticket_api.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Page<RefreshToken> findAllByUser(Pageable pageable, Users user);
    @Modifying
    void deleteByUser(Users user);
}
