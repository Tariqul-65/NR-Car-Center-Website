package com.example.nrcarcenter.config;

import com.example.nrcarcenter.entity.AdminUser;
import com.example.nrcarcenter.entity.Role;
import com.example.nrcarcenter.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevAdminSeeder implements CommandLineRunner {

    private static final String EMAIL = "admin@nrcarcenter.com";
    private static final String PASSWORD = "Admin@123";

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (adminUserRepository.findByEmailIgnoreCase(EMAIL).isPresent()) return;

        AdminUser admin = new AdminUser();
        admin.setFullName("Super Admin");
        admin.setEmail(EMAIL);
        admin.setPhone("+8801700000000");
        admin.setPasswordHash(passwordEncoder.encode(PASSWORD));
        admin.setRole(Role.SUPER_ADMIN);
        admin.setEnabled(true);
        admin.setAccountNonLocked(true);
        admin.setFailedAttempts(0);
        admin.setCreatedAt(Instant.now());

        adminUserRepository.save(admin);

        System.out.println("✅ DEV SUPER ADMIN CREATED");
        System.out.println("Email: " + EMAIL);
        System.out.println("Password: " + PASSWORD);
    }
}
