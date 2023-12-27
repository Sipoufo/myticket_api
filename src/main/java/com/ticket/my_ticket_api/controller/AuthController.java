package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.entity.ERole;
import com.ticket.my_ticket_api.entity.RefreshToken;
import com.ticket.my_ticket_api.entity.Role;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.exception.TokenRefreshException;
import com.ticket.my_ticket_api.payload.request.*;
import com.ticket.my_ticket_api.payload.response.JwtAuthenticationResponse;
import com.ticket.my_ticket_api.payload.response.MessageResponse;
import com.ticket.my_ticket_api.payload.response.TokenVerifyResponse;
import com.ticket.my_ticket_api.payload.response.UserMachineDetails;
import com.ticket.my_ticket_api.repository.RoleRepository;
import com.ticket.my_ticket_api.security.jwt.JwtUtils;
import com.ticket.my_ticket_api.service.refreshTokenService.RefreshTokenService;
import com.ticket.my_ticket_api.service.refreshTokenService.RefreshTokenServiceImpl;
import com.ticket.my_ticket_api.service.userService.UserService;
import com.ticket.my_ticket_api.service.userService.UserServiceImpl;
import com.ticket.my_ticket_api.utils.HttpUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import eu.bitwalker.useragentutils.UserAgent;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private final UserService userService = new UserServiceImpl();
    @Autowired
    private final RefreshTokenService refreshTokenService = new RefreshTokenServiceImpl();

    @Autowired
    RoleRepository roleRepository;

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Value("${frontEnd.url}")
    private String frontUrl;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ipAddress = HttpUtils.getClientIp();

        UserMachineDetails userMachineDetails = UserMachineDetails
                .builder()
                .ipAddress(ipAddress)
                .browser(userAgent.getBrowser().getName())
                .operatingSystem(userAgent.getOperatingSystem().getName())
                .build();

        Optional<Users> user = userService.getUserByEmail(loginRequest.getUsername());

        if (user.isEmpty() || !encoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email or password error"));
        }

        if (user.get().isDeleted()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Your are blocked"));
        }

        String jwt = jwtUtils.generateJwtToken(user.get().getEmail());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.get().getEmail(), userMachineDetails);

        System.out.println(refreshToken.getToken());
        return ResponseEntity.ok(JwtAuthenticationResponse.builder().token(jwt).user(user.get()).refreshToken(refreshToken.getToken()).firstName(user.get().getFirstName()).build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ipAddress = HttpUtils.getClientIp();
        System.out.println("je passe");

        UserMachineDetails userMachineDetails = UserMachineDetails
                .builder()
                .ipAddress(ipAddress)
                .browser(userAgent.getBrowser().getName())
                .operatingSystem(userAgent.getOperatingSystem().getName())
                .build();
        System.out.println("je passe");

        if (userService.isEmailExisted(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email is already in use!"));
        }
        System.out.println("je passe 2");

        if (userService.isPhoneExisted(signUpRequest.getPhone())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Phone is already in use!"));
        }
        System.out.println(signUpRequest.getPassword());

        // Create new user's account
        Users user = Users
                .builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        String strRoles = signUpRequest.getRole();
        Role roles = null;
        System.out.println("je passe3");

        if (strRoles == null) {
            roles = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found."));
        } else {
            if (strRoles.equals("admin")) {
                roles = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Role is not found."));
            } else {
                roles = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Role is not found."));
            }
        }
        System.out.println("je passe 4");

        user.setRole(roles);
        user.setToken_validation(jwtUtils.generateJwtTokenValidation(user.getEmail()));
        userService.createUser(user);
        System.out.println("je passe 5");

        String jwt = jwtUtils.generateJwtToken(user.getEmail());
        String subject = "Here's the link for account validation";
        String content = "<p>Hello,</p>"
                + "<p>Welcome to my ticket.</p>"
                + "<p>Click the link below to validate your account:</p>"
                + "<p><a href=\"" + frontUrl + jwt + "\">Validation.</a></p>";

        try {
            userService.sendEmail(user.getEmail(), subject, content);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Connection problem please try later!"));
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail(), userMachineDetails);

        return ResponseEntity.ok(JwtAuthenticationResponse.builder().token(jwt).user(user).refreshToken(refreshToken.getToken()).firstName(user.getFirstName()).build());
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<?> createAdmin(@Validated @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ipAddress = HttpUtils.getClientIp();

        UserMachineDetails userMachineDetails = UserMachineDetails
                .builder()
                .ipAddress(ipAddress)
                .browser(userAgent.getBrowser().getName())
                .operatingSystem(userAgent.getOperatingSystem().getName())
                .build();
        System.out.println("je passe ++");
        System.out.println(userService.isEmailExisted(signUpRequest.getEmail()));
        System.out.println("je passe ++");

        if (userService.isEmailExisted(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email is already in use!"));
        }
        System.out.println("je passe 2");

        if (userService.isPhoneExisted(signUpRequest.getPhone())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Phone is already in use!"));
        }
        System.out.println(signUpRequest.getPassword());

        // Create new user's account
        String password = "ChaIOçh@pT!chEt#2023";
        Users user = Users
                .builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .password(encoder.encode(password))
                .role(roleRepository.findByName(ERole.ROLE_ADMIN).get())
                .build();

        user.setToken_validation(jwtUtils.generateJwtTokenValidation(user.getEmail()));
        userService.createUser(user);
        System.out.println("je passe 5");

        String jwt = jwtUtils.generateJwtToken(user.getEmail());
        String subject = "Your admin identify";
        String content = "<p>Hello, "+signUpRequest.getFirstName()+"</p>"
                + "<p>Welcome to my chapchapTicket.</p>"
                + "<p>Your password is: "+password+"</p>";

        try {
            userService.sendEmail(user.getEmail(), subject, content);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Connection problem please try later!"));
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail(), userMachineDetails);

        return ResponseEntity.ok(JwtAuthenticationResponse.builder().token(jwt).user(user).refreshToken(refreshToken.getToken()).firstName(user.getFirstName()).build());
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) throws MessagingException, UnsupportedEncodingException {
        return userService.forgetPassword(forgetPasswordRequest.getEmail());
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return userService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmPassword());
    }

    @PostMapping("/verifyAccessToken")
    public ResponseEntity<?> VerifyAccessToken(@RequestHeader (name="Authorization") String token) {
        System.out.println(token.substring(7));
        boolean isValidated = jwtUtils.validateJwtToken(token.substring(7));
        System.out.println(isValidated);
        if (isValidated) {
            return ResponseEntity
                .ok()
                .body(
                    TokenVerifyResponse
                        .builder()
                        .isValid(true)
                        .message("Is Valid")
                        .build()
                );
        }
        return ResponseEntity
            .ok()
            .body(
                TokenVerifyResponse
                    .builder()
                    .isValid(false)
                    .message("Is expired")
                    .build()
            );
    }

    @PostMapping("/verifyRefreshToken")
    public ResponseEntity<?> VerifyRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken());
        if (refreshToken.isEmpty() || refreshTokenService.verifyExpiration(refreshToken.get()) == null) {
            return ResponseEntity
                    .ok()
                    .body(
                        TokenVerifyResponse
                            .builder()
                            .isValid(false)
                            .message("RefreshToken not found")
                            .build()
                    );
        }

        return ResponseEntity
                .ok()
                .body(
                    TokenVerifyResponse
                        .builder()
                        .isValid(true)
                        .message("Is not expired")
                        .build()
                );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        JwtAuthenticationResponse jwtAuthenticationResponse = refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map((user) -> {
                    String token = jwtUtils.generateJwtToken(user.getEmail());
                    return JwtAuthenticationResponse.builder().token(token).user(user).refreshToken(requestRefreshToken).build();
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
        return ResponseEntity.ok(jwtAuthenticationResponse);
    }
}
