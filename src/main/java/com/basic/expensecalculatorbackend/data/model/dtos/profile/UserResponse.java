package com.basic.expensecalculatorbackend.data.model.dtos.profile;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private Long id;
    private String fullName;
    private String pinHash;
}
