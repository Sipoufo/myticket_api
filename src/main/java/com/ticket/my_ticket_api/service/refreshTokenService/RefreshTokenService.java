package com.ticket.my_ticket_api.service.refreshTokenService;

import com.ticket.my_ticket_api.entity.RefreshToken;
import com.ticket.my_ticket_api.payload.response.UserMachineDetails;

import java.util.Optional;

public interface RefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(String email, UserMachineDetails userMachineDetails);
    public RefreshToken verifyExpiration(RefreshToken token);
    public void deleteByUserId(String email);
}
