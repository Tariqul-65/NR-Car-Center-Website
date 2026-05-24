package com.example.nrcarcenter.repository;


import com.example.nrcarcenter.entity.AdminUser;
import com.example.nrcarcenter.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhone(String phone);

    List<AdminUser> findByRole(Role role);
    List<AdminUser> findByFullNameContainingIgnoreCase(String q);
    List<AdminUser> findByEmailContainingIgnoreCase(String q);
    List<AdminUser> findByPhoneContaining(String q);
}
