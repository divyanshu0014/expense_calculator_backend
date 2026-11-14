package com.basic.expensecalculatorbackend.controller.userController;


import com.basic.expensecalculatorbackend.data.dtos.profile.CreateUserRequest;
import com.basic.expensecalculatorbackend.data.dtos.profile.UserResponse;
import com.basic.expensecalculatorbackend.services.userServices.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserResponse>> getUserProfiles() {
        return new ResponseEntity<List<UserResponse>>(
                userService.getUserProfiles(),
                HttpStatus.FOUND
        );
    }

    @PostMapping("/profile")
    public ResponseEntity<UserResponse> createUserProfile(@RequestBody CreateUserRequest createUserRequest) {
        return new ResponseEntity<UserResponse>(
                userService.createUserProfile(createUserRequest),
                HttpStatus.CREATED
        );
    }


}
