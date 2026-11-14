package com.basic.expensecalculatorbackend.services.userServices;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PinService {

    PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();


    public String hashPin(String pin) {
        validatePin(pin);
        return passwordEncoder.encode(pin);
    }

    public boolean verifyPin(String rawPin, String hashedPin) {
        validatePin(rawPin);
        return passwordEncoder.matches(rawPin, hashedPin);
    }

    private void validatePin(String rawPin) {
        if (Objects.isNull(rawPin) || !rawPin.matches("^\\d{4,8}$")) {
            throw new IllegalArgumentException("Invalid PIN format");
        }
    }
}
