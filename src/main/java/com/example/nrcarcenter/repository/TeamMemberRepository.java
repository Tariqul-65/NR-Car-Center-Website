package com.example.nrcarcenter.repository;

import com.example.nrcarcenter.entity.TeamMember;
import com.example.nrcarcenter.entity.TeamRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByNameContainingIgnoreCase(String q);
    List<TeamMember> findByPhoneContaining(String q);
    List<TeamMember> findByEmailContainingIgnoreCase(String q);
    List<TeamMember> findByRole(TeamRole role);

    boolean existsByPhone(String phone);
}
