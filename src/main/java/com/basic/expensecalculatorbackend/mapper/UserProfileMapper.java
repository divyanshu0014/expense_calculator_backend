package com.basic.expensecalculatorbackend.mapper;

import com.basic.expensecalculatorbackend.data.dtos.profile.UserResponse;
import com.basic.expensecalculatorbackend.data.entities.UserProfileEntity;
import com.basic.expensecalculatorbackend.data.model.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileEntity toEntity(UserProfile profile, UserProfileEntity existingEntity) {
        if (existingEntity == null) {
            existingEntity = new UserProfileEntity();
        }
        existingEntity.setName(profile.getName());
        existingEntity.setPin(profile.getPinHash());
        return existingEntity;
    }

    public UserProfile toModel(UserProfileEntity profileEntity) {
        if (profileEntity == null) {
            return null;
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setName(profileEntity.getName());
        userProfile.setPinHash(profileEntity.getPin());
        return userProfile;
    }

    public UserResponse toResponse(UserProfileEntity profileEntity) {
        if (profileEntity == null) {
            return null;
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setId(profileEntity.getId());
        userResponse.setFullName(profileEntity.getName());
        userResponse.setPinHash(profileEntity.getPin());
        return userResponse;
    }
}
