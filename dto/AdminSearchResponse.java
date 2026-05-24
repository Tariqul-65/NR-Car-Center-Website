package com.example.nrcarcenter.dto;


import com.example.nrcarcenter.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSearchResponse {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private Role role;
    private String profilePhotoUrl;
    private boolean enabled;
    private boolean accountNonLocked;
}
