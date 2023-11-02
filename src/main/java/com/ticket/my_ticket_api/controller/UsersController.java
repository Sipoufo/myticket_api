package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.payload.request.ForgetPasswordRequest;
import com.ticket.my_ticket_api.payload.request.UserSetting;
import com.ticket.my_ticket_api.service.userService.UserService;
import com.ticket.my_ticket_api.service.userService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    private final UserService userService = new UserServiceImpl();

    @GetMapping("")
    public ResponseEntity<?> getOneUserByToken(@RequestHeader (name="Authorization") String token) {
        return userService.getOneUserByToken(token);
    }
    @PutMapping("")
    public ResponseEntity<?> updateOneUserByToken(@RequestBody UserSetting userSetting, @RequestHeader (name="Authorization") String token) {
        return userService.updateSettingUser(userSetting, token);
    }
}
