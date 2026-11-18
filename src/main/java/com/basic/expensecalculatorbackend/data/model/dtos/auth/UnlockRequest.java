package com.basic.expensecalculatorbackend.data.model.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnlockRequest {
    @NotBlank
    private String rawPin;

}
