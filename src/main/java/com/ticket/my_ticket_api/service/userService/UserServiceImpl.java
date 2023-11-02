package com.ticket.my_ticket_api.service.userService;

import com.ticket.my_ticket_api.entity.Ticket;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.exception.ResourceNotFoundException;
import com.ticket.my_ticket_api.payload.request.UserSetting;
import com.ticket.my_ticket_api.payload.response.MessageResponse;
import com.ticket.my_ticket_api.repository.TicketRepository;
import com.ticket.my_ticket_api.repository.UserRepository;
import com.ticket.my_ticket_api.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JwtUtils jwtUtils;

    @Value("${frontEnd.url}")
    private String frontUrl;

    @Override
    public HttpStatus createUser(Users user) {
        userRepository.save(user);
        return HttpStatus.CREATED;
    }

    @Override
    public List<Users> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    @Override
    public ResponseEntity<?> getOneUser(long userId) {
        return ResponseEntity.ok(userRepository.findById(userId));
    }

    @Override
    public ResponseEntity<?> getOneUserByToken(String token) {
        Optional<Users> organizer = getUserByToken(token);
        if (organizer.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not authenticate !"));
        }
        return ResponseEntity.ok(userRepository.findById(organizer.get().getUserId()));
    }

    @Override
    public HttpStatus updateSUser(Users user, long userId) {
        Optional<Users> user1 = userRepository.findById(userId);

        if (user1.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        user1.get().setFirstName(user.getFirstName());
        user1.get().setLastName(user.getLastName());
        user1.get().setPhone(user.getPhone());
        user1.get().setWebsite(user.getWebsite());
        user1.get().setCompany(user.getCompany());
        user1.get().set_organizer(user.is_organizer());
        user1.get().setPosition(user.getPosition());
        user1.get().setAddress(user.getAddress());
        user1.get().setAddress2(user.getAddress2());
        user1.get().setCity(user.getCity());
        user1.get().setCode_postal(user.getCode_postal());
        user1.get().setState(user.getState());
        user1.get().setPicture(user.getPicture());
        userRepository.save(user1.get());

        return HttpStatus.OK;
    }

    @Override
    public ResponseEntity<?> updateSettingUser(UserSetting user, String token)  {
        Optional<Users> user1 = getUserByToken(token);
        if (user1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not authenticate !"));
        }
        user1.get().setFirstName(user.getFirstName());
        user1.get().setLastName(user.getLastName());
        user1.get().setPhone(user.getPhone());
        user1.get().setWebsite(user.getWebsite());
        user1.get().setCompany(user.getCompany());
        user1.get().setPosition(user.getPosition());
        user1.get().setAddress(user.getAddress());
        user1.get().setAddress2(user.getAddress2());
        user1.get().setCity(user.getCity());
        user1.get().setCode_postal(user.getCode_postal());
        user1.get().setState(user.getState());
        userRepository.save(user1.get());
        return ResponseEntity.ok(MessageResponse.builder().message("Your profile is successful updated").build());
    }

    @Override
    public HttpStatus deleteUser(long userId) {
        Optional<Users> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        userRepository.deleteById(userId);
        return HttpStatus.OK;
    }

    @Override
    public boolean isEmailExisted(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isPhoneExisted(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public Optional<Users> getUserByToken(String token) {
        if (token.length() < 7) {
            return Optional.empty();
        }
        if (!jwtUtils.validateJwtToken(token.substring(7))) {
            return Optional.empty();
        }
        String email = jwtUtils.getEmailFromJwtToken(token.substring(7));
        return getUserByEmail(email);
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public HttpStatus addUserByUser(long userId, long ticketId) {
        Optional<Users> user = userRepository.findById(ticketId);

        if (user.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        Optional<Ticket> ticket = ticketRepository.findById(userId);
        if (ticket.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        user.get().addTicket(ticket.get());
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus RemoveUserByUser(long userId, long ticketId) {
        Optional<Users> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        Optional<Ticket> ticket = ticketRepository.findById(userId);
        if (ticket.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        user.get().removeTicket(ticket.get().getTicket_id());
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus ResetPassword(String oldPassword, String newPassword) {
        return null;
    }

    @Override
    public List<Users> getAllUsersByRoleId(long roleId) {
        return userRepository.findByRoleRoleId(roleId);
    }

    @Override
    public HttpStatus deleteAllUsersByRoleId(long roleId) {
        userRepository.deleteByRoleRoleId(roleId);
        return HttpStatus.OK;
    }

    @Override
    public ResponseEntity<?> forgetPassword(String email) throws MessagingException, UnsupportedEncodingException {
        Optional<Users> user = getUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This email don't exist!"));
        }
        String token = jwtUtils.generateJwtToken(user.get().getEmail(), 3600000);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + frontUrl + token + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        sendEmail(email, subject, content );
        user.get().setRestart_password_token(token);
        userRepository.save(user.get());

        return ResponseEntity.ok(new MessageResponse("Your will receive email for reset your password."));
    }

    @Override
    public ResponseEntity<?> resetPassword(String token, String newPassword, String confirmPassword) {
        if (!jwtUtils.validateJwtToken(token)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You have take lot of time, retry!"));
        }
        if (!Objects.equals(newPassword, confirmPassword)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You new and confirm password are not same !"));
        }
        String email = jwtUtils.getEmailFromJwtToken(token);

        Optional<Users> user = getUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Please contact administrator!"));
        }
        if (user.get().getRestart_password_token() == null || !Objects.equals(user.get().getRestart_password_token(), token)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Your have already change your password!"));
        }
        user.get().setPassword(encoder.encode(newPassword));
        user.get().setRestart_password_token(null);
        userRepository.save(user.get());

        return ResponseEntity.ok(new MessageResponse("Your password is successful reset."));
    }

    @Override
    public void sendEmail(String recipientEmail, String subject, String content)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("sipoufoknj@gmail.com", "MyTicket Support");
        helper.setTo(recipientEmail);

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public Users getOneUserL(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + email));
    }
}
