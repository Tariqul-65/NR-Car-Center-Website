package com.example.nrcarcenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_members", indexes = {
        @Index(name = "idx_team_phone", columnList = "phone", unique = true),
        @Index(name = "idx_team_role", columnList = "role")
})
public class TeamMember extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = true, length = 160)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TeamRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ActiveStatus status;

    @Column(nullable = true, length = 255)
    private String photoPath;
}
