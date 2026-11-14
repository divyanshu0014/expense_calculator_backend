package com.basic.expensecalculatorbackend.data.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfile {

    private Long id;
    private String name;
    private String pinHash;
}
