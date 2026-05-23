package com.example.nrcarcenter.dto;


import com.example.nrcarcenter.entity.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteCreateRequest {
    @NotNull
    private Role role;

    @Positive
    private int validHours;
}
