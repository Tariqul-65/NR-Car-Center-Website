package com.example.nrcarcenter.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    @Size(max = 120)
    private String fullName;

    @Size(max = 240)
    private String address;
}
