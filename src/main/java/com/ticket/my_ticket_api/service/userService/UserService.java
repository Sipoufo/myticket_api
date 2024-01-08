package com.ticket.my_ticket_api.service.userService;

import com.ticket.my_ticket_api.entity.ERole;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.payload.request.UserCrucialInfo;
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
    public ResponseEntity<?> getAllUsers_admin(Pageable pageable, String token);
    public ResponseEntity<?> getUsersInfo_admin(String token);
    public ResponseEntity<?> getAdminsInfo_admin(String token);
    public ResponseEntity<?> getOneUser(long userId);
    public ResponseEntity<?> getOneUserByToken(String token);
    public ResponseEntity<?> getUserType(String token);
    public HttpStatus updateSUser(Users user, long userId);
    public ResponseEntity<?> updateSettingUser(UserSetting user, String token);
    public ResponseEntity<?> updateCrucialInfoUser(UserCrucialInfo userCrucialInfo, String token);
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
    public ResponseEntity<?> findByRole_customer(boolean isForAdmin, String token, Pageable pageable);
    public ResponseEntity<?> forgetPassword(String email) throws MessagingException, UnsupportedEncodingException;
    public ResponseEntity<?> resetPassword(String token, String newPassword, String confirmPassword);
    public ResponseEntity<?> blockUser(String token, long userId, boolean isBlock);

    public ResponseEntity<?> searchUser(ERole name, String token, String searchWord, Pageable pageable);
    public ResponseEntity<?> findUserByUserId(String token, long userId);

}
