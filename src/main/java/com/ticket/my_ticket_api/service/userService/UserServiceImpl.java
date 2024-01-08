package com.ticket.my_ticket_api.service.userService;

import com.ticket.my_ticket_api.entity.ERole;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.exception.ResourceNotFoundException;
import com.ticket.my_ticket_api.payload.request.UserCrucialInfo;
import com.ticket.my_ticket_api.payload.request.UserSetting;
import com.ticket.my_ticket_api.payload.response.AdminsInfoResponse;
import com.ticket.my_ticket_api.payload.response.DataResponse;
import com.ticket.my_ticket_api.payload.response.MessageResponse;
import com.ticket.my_ticket_api.payload.response.UsersInfoResponse;
import com.ticket.my_ticket_api.repository.EventRepository;
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

    @Value("${frontEnd.admin.url}")
    private String adminUrl;
    @Autowired
    private EventRepository eventRepository;

    @Override
    public HttpStatus createUser(Users user) {
        userRepository.save(user);
        return HttpStatus.CREATED;
    }

    @Override
    public ResponseEntity<?> getAllUsers_admin(Pageable pageable, String token) {
        Optional<Users> user = getUserByToken(token);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not authenticate !"));
        }
        if (user.get().getRole().getName() != ERole.ROLE_ADMIN) {return ResponseEntity
                .badRequest()
                .body(new MessageResponse("You don't have this privilege !"));
        }
        return ResponseEntity.ok(DataResponse
                .builder()
                        .data(userRepository.findAll(pageable).getContent())
                        .dataNumber(userRepository.findAll(pageable).getContent().size())
                        .actualPage(pageable.getPageNumber() + 1)
                        .pageable(pageable)
                .build());
    }

    @Override
    public ResponseEntity<?> getUsersInfo_admin(String token) {
        Optional<Users> user = getUserByToken(token);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not authenticate !"));
        }
        if (user.get().getRole().getName() != ERole.ROLE_ADMIN) {return ResponseEntity
                .badRequest()
                .body(new MessageResponse("You don't have this privilege !"));
        }

        int userNumber = userRepository.findByRoleNameIsNotAndUserIdIsNot(ERole.ROLE_ADMIN, user.get().getUserId()).size();
        int eventNumber = eventRepository.findAll().size();
        int ticketNumber = ticketRepository.findAll().size();

        return ResponseEntity.ok(UsersInfoResponse
                .builder()
                        .userNumber(userNumber)
                        .eventNumber(eventNumber)
                        .ticketNumber(ticketNumber)
                .build());
    }

    @Override
    public ResponseEntity<?> getAdminsInfo_admin(String token) {
        Optional<Users> user = getUserByToken(token);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not authenticate !"));
        }
        if (user.get().getRole().getName() != ERole.ROLE_ADMIN) {return ResponseEntity
                .badRequest()
                .body(new MessageResponse("You don't have this privilege !"));
        }

        int adminNumber = userRepository.findByRoleName(ERole.ROLE_ADMIN).size();
        int adminActiveNumber = userRepository.findByRoleNameAndIsDeleted(ERole.ROLE_ADMIN, false).size();
        int adminBlockNumber = userRepository.findByRoleNameAndIsDeleted(ERole.ROLE_ADMIN, true).size();

        return ResponseEntity.ok(AdminsInfoResponse
                .builder()
                        .adminNumber(adminNumber)
                        .adminActiveNumber(adminActiveNumber)
                        .adminBlockNumber(adminBlockNumber)
                .build());
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

    public ResponseEntity<?> getUserType(String token){
        Optional<Users> organizer = getUserByToken(token);
        if (organizer.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not authenticate !"));
        }
        return ResponseEntity.ok(userRepository.findById(organizer.get().getUserId()).get().getEStateOrganiser());
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

    public ResponseEntity<?> updateCrucialInfoUser(UserCrucialInfo userCrucialInfo, String token) {
        Optional<Users> user1 = getUserByToken(token);
        if (user1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not authenticate !"));
        }

        String subject = "Email / Password updated";
        String content = "<p>Hello </p>" + user1.get().getFirstName()
                + "<p>Your email / password has been updated successfully</p>";

        String email = userCrucialInfo.getEmail() != null ? userCrucialInfo.getEmail() : user1.get().getEmail();
        user1.get().setEmail(email);
        user1.get().setPassword(userCrucialInfo.getPassword() != null ? encoder.encode(userCrucialInfo.getPassword()) : user1.get().getPassword());

        userRepository.save(user1.get());

        try {
            sendEmail(email, subject, content);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Connection problem please try later!"));
        }
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
        System.out.println("token => "+token);
        if (token.length() < 7) {
            return Optional.empty();
        }
        if (!jwtUtils.validateJwtToken(token.substring(7))) {
            return Optional.empty();
        }
        String email = jwtUtils.getEmailFromJwtToken(token.substring(7));
        System.out.println("email => "+email);
        return getUserByEmail(email);
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
    public ResponseEntity<?> findByRole_customer(boolean isForAdmin, String token, Pageable pageable) {
        Optional<Users> user = getUserByToken(token);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not administrator !"));
        }

        if (isForAdmin) {
            return ResponseEntity.ok(DataResponse
                    .builder()
                    .data(userRepository.findByRoleNameIsNotAndUserIdIsNot(ERole.ROLE_USER, user.get().getUserId(), pageable))
                    .dataNumber(userRepository.findByRoleNameIsNotAndUserIdIsNot(ERole.ROLE_USER, user.get().getUserId(), pageable).size())
                    .actualPage(pageable.getPageNumber() + 1)
                    .pageable(pageable)
                    .build());
        } else {
            return ResponseEntity.ok(DataResponse
                    .builder()
                    .data(userRepository.findByRoleNameIsNotAndUserIdIsNot(ERole.ROLE_ADMIN, user.get().getUserId(), pageable))
                    .dataNumber(userRepository.findByRoleNameIsNotAndUserIdIsNot(ERole.ROLE_ADMIN, user.get().getUserId(), pageable).size())
                    .actualPage(pageable.getPageNumber() + 1)
                    .pageable(pageable)
                    .build());
        }
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

        String link = (Objects.equals(user.get().getRole().getName().toString(), "ROLE_ADMIN")) ? adminUrl : frontUrl;

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + token + "\">Change my password</a></p>"
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

    @Override
    public ResponseEntity<?> blockUser(String token, long userId, boolean isBlock){
        System.out.println("Je passe Block");
        Optional<Users> user1 = getUserByToken(token);
        if (user1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not administrator !"));
        }
        Optional<Users> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User don't found !"));
        }

        user.get().setDeleted(isBlock);
        userRepository.save(user.get());

        return ResponseEntity.ok(MessageResponse
                .builder()
                .message(isBlock ? "User block" : "User unblock")
                .build());
    }

    @Override
    public ResponseEntity<?> searchUser(ERole name, String token, String searchWord, Pageable pageable) {
        Optional<Users> user = getUserByToken(token);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not administrator !"));
        }
        List<Users> users = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAndRoleNameIsNotAndUserIdIsNot(searchWord, searchWord, searchWord, name, user.get().getUserId(), pageable);
        return ResponseEntity.ok(
                DataResponse
                        .builder()
                        .data(users)
                        .actualPage(pageable.getPageNumber() + 1)
                        .dataNumber(users.size())
                        .pageable(pageable)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> findUserByUserId(String token, long userId) {
        Optional<Users> user1 = getUserByToken(token);
        if (user1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not administrator !"));
        }
        Optional<Users> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You are not authenticate !"));
        }
        return ResponseEntity.ok(user.get());
    }
}
