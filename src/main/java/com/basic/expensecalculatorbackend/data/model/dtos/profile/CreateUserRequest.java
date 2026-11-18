package com.basic.expensecalculatorbackend.data.model.dtos.profile;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateUserRequest {
    private String fullName;
    private String securityPin;
}
