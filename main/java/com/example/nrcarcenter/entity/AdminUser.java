package com.example.nrcarcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_users",
        indexes = {
                @Index(name = "idx_admin_email", columnList = "email", unique = true),
                @Index(name = "idx_admin_phone", columnList = "phone", unique = true)
        })
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, length = 30, unique = true)
    private String phone;

    @Column(nullable = false, length = 180, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Column(nullable = false, length = 200)
    private String passwordHash;

    @Column(length = 240)
    private String address;

    @Column(length = 500)
    private String profilePhotoPath;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean accountNonLocked;

    @Column(nullable = false)
    private int failedAttempts;

    private Instant lastLoginAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        enabled = true;
        accountNonLocked = true;
        failedAttempts = 0;
    }
}
