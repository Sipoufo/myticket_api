package com.ticket.my_ticket_api.service.userService;

import com.ticket.my_ticket_api.entity.Event;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.payload.request.UserSetting;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public HttpStatus createUser(Users user);
    public List<Users> getAllUsers(Pageable pageable);
    public ResponseEntity<?> getOneUser(long userId);
    public ResponseEntity<?> getOneUserByToken(String token);
    public HttpStatus updateSUser(Users user, long userId);
    public ResponseEntity<?> updateSettingUser(UserSetting user, String token);
    public HttpStatus deleteUser(long userId);
    public boolean isEmailExisted(String email);
    public boolean isPhoneExisted(String phone);
    public Optional<Users> getUserByToken(String token);
    public Optional<Users> getUserByEmail(String email);
    public HttpStatus ResetPassword(String oldPassword, String newPassword);
    public List<Users> getAllUsersByRoleId(long roleId);
    public HttpStatus deleteAllUsersByRoleId(long roleId);
    public void sendEmail(String recipientEmail, String subject, String content) throws MessagingException, UnsupportedEncodingException;

    //Authentication
    public ResponseEntity<?> forgetPassword(String email) throws MessagingException, UnsupportedEncodingException;
    public ResponseEntity<?> resetPassword(String token, String newPassword, String confirmPassword);
}
