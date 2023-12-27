package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.entity.ERole;
import com.ticket.my_ticket_api.payload.request.UserCrucialInfo;
import com.ticket.my_ticket_api.payload.request.UserSetting;
import com.ticket.my_ticket_api.service.userService.UserService;
import com.ticket.my_ticket_api.service.userService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @PutMapping("/crucial")
    public ResponseEntity<?> updateUserCrucialInfoByToken(@RequestBody UserCrucialInfo userCrucialInfo, @RequestHeader (name="Authorization") String token) {
        return userService.updateCrucialInfoUser(userCrucialInfo, token);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getAllUsers_admin(@RequestHeader (name="Authorization") String token, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        System.out.println("Je passe 2");
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return userService.getAllUsers_admin(pageable, token);
    }

    @GetMapping("/customer/{isForAdmin}/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getAllCustomer(@RequestHeader (name="Authorization") String token, @PathVariable("isForAdmin") boolean isForAdmin, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        System.out.println("Je passe 4");
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return userService.findByRole_customer(isForAdmin, token, pageable);
    }

    @GetMapping("/userInfo")
    public ResponseEntity<?> getUsersInfo_admin(@RequestHeader (name="Authorization") String token) {
        return userService.getUsersInfo_admin(token);
    }

    @GetMapping("/adminInfo")
    public ResponseEntity<?> getAdminsInfo_admin(@RequestHeader (name="Authorization") String token) {
        return userService.getAdminsInfo_admin(token);
    }

    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<?> getUsersInfo_admin(@PathVariable("userId") long userId) {
        return userService.getOneUser(userId);
    }

    @PutMapping("/block/{userId}/{block}")
    public ResponseEntity<?> blockUser(@RequestHeader (name="Authorization") String token, @PathVariable("userId") long userId, @PathVariable("block") boolean block) {
        return userService.blockUser(token, userId, block);
    }

    @GetMapping("/search/{role}/{searchWord}/{pageNumber}/{pageSize}")
    public ResponseEntity<?> searchUser_admin(@RequestHeader (name="Authorization") String token, @PathVariable("role") ERole role, @PathVariable("searchWord") String searchWord, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        System.out.println("Je passe search");
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return userService.searchUser(role, token, searchWord, pageable);
    }

    @GetMapping("/oneUser/{userId}")
    public ResponseEntity<?> blockUser(@RequestHeader (name="Authorization") String token, @PathVariable("userId") long userId) {
        return userService.findUserByUserId(token, userId);
    }
}
