package com.example.nrcarcenter.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank
    @Size(max = 120)
    private String fullName;

    @NotBlank
    @Size(max = 30)
    private String phone;

    @NotBlank
    @Email
    @Size(max = 180)
    private String email;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

    @NotBlank
    @Size(min = 8, max = 72)
    private String confirmPassword;

    @Size(max = 240)
    private String address;

    @NotBlank
    @Size(max = 120)
    private String inviteToken;
}
