package com.ticket.my_ticket_api.security.jwt;

import com.ticket.my_ticket_api.exception.BadRequestException;
import com.ticket.my_ticket_api.exception.ExceptionDetails;
import com.ticket.my_ticket_api.exception.TokenExpiredExceptionDetails;
import com.ticket.my_ticket_api.security.services.UserDetailsServiceImpl;
import com.ticket.my_ticket_api.utils.ExceptionUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getEmailFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println(SecurityContextHolder.getContext().getAuthentication());
                System.out.println("Ok 444");
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            System.out.println("ok");
            logger.error("Cannot set user authentication: " + ex );
            HttpStatus status = HttpStatus.UNAUTHORIZED;

            ExceptionDetails details = ExceptionDetails.createExceptionDetails(ex, status, "Unauthorized.");
            TokenExpiredExceptionDetails tokenDetails = TokenExpiredExceptionDetails
                    .builder()
                    .title(details.getTitle())
                    .details(details.getDetails())
                    .status(status.value())
                    .timestamp(details.getTimestamp())
                    .developerMessage(details.getDeveloperMessage())
                    .expired(true)
                    .build();

            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(ExceptionUtils.convertObjectToJson(tokenDetails));
        } catch (Exception e) {
            System.out.println("ok");
            logger.error("Cannot set user authentication: " + e );
            filterChain.doFilter(request, response);
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
