package com.basic.expensecalculatorbackend.controller.userController;


import com.basic.expensecalculatorbackend.data.model.dtos.auth.UnlockRequest;
import com.basic.expensecalculatorbackend.data.model.dtos.auth.AuthResponse;
import com.basic.expensecalculatorbackend.services.userServices.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/unlock/{id}")
    private ResponseEntity<AuthResponse> unlockUserProfile(
            @PathVariable("id") Long id, @RequestBody UnlockRequest request) {
        return new ResponseEntity<AuthResponse> (
            userService.authenticateUser(id, request.getRawPin()),
            HttpStatus.OK
        );
    }
}
