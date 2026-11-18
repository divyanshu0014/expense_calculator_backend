package com.basic.expensecalculatorbackend.services.userServices;

import com.basic.expensecalculatorbackend.data.model.dtos.auth.AuthResponse;
import com.basic.expensecalculatorbackend.data.model.dtos.profile.CreateUserRequest;
import com.basic.expensecalculatorbackend.data.model.dtos.profile.UserResponse;
import com.basic.expensecalculatorbackend.data.model.entities.UserProfileEntity;
import com.basic.expensecalculatorbackend.data.model.UserProfile;
import com.basic.expensecalculatorbackend.mapper.UserProfileMapper;
import com.basic.expensecalculatorbackend.repositories.UserProfileRepository;
import com.basic.expensecalculatorbackend.security.JwtService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final PinService pinService;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final JwtService jwtService;

    public UserService(PinService pinService, UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper, JwtService jwtService) {
        this.pinService = pinService;
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
        this.jwtService = jwtService;
    }

    public UserResponse createUserProfile(CreateUserRequest newUserRequest) {
        if (Objects.isNull(newUserRequest)) {
            throw new IllegalArgumentException("Invalid create user profile request");
        }

        UserProfile newUserProfile = new UserProfile();
        newUserProfile.setName(newUserRequest.getFullName());
        newUserProfile.setPinHash(pinService.hashPin(newUserRequest.getSecurityPin()));

        UserProfileEntity savedUser = userProfileRepository.save(
                userProfileMapper.toEntity(newUserProfile, null));

        return userProfileMapper.toResponse(savedUser);
    }

    public List<UserResponse> getUserProfiles() {
        List<UserProfileEntity> userProfileEntities = userProfileRepository.findAll();
        return userProfileEntities.stream()
                .map(userProfileMapper::toResponse).toList();
    }

    public AuthResponse authenticateUser(Long id, String rawPin) {
        if (Objects.isNull(id) || Objects.isNull(rawPin)) {
            throw new IllegalArgumentException("Invalid user profile request");
        }

        UserProfileEntity entity = userProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user profile request"));

        if (!pinService.verifyPin(rawPin, entity.getPin())) {
            throw new IllegalArgumentException("Invalid security pin");
        }
        //Authentication successful
        String token = jwtService.generateToken(entity.getName(), entity.getId());
        return new AuthResponse(token);
    }

}
