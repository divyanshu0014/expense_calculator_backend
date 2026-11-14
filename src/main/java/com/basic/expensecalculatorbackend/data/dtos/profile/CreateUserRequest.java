package com.basic.expensecalculatorbackend.data.dtos.profile;


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
