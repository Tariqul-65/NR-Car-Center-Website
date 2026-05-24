package com.example.nrcarcenter.service;

import com.example.nrcarcenter.dto.AdminSearchResponse;
import com.example.nrcarcenter.dto.RegisterRequest;
import com.example.nrcarcenter.dto.UpdateProfileRequest;
import com.example.nrcarcenter.entity.AdminUser;
import com.example.nrcarcenter.entity.InvitationToken;
import com.example.nrcarcenter.repository.AdminUserRepository;
import com.example.nrcarcenter.repository.DeliveredCarRepository;
import com.example.nrcarcenter.repository.StockCarRepository;
import com.example.nrcarcenter.repository.TeamMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository repo;
    private final InvitationService invitationService;

    private final PasswordEncoder encoder;

    private final StockCarRepository stockRepo;
    private final DeliveredCarRepository deliveredRepo;

    private final TeamMemberRepository teamRepo;

    @Transactional
    public AdminUser register(RegisterRequest req, MultipartFile profilePhoto) {
        if (req == null) throw new IllegalArgumentException("Invalid request");

        String fullName = clean(req.getFullName());
        String phone = clean(req.getPhone());
        String email = cleanLower(req.getEmail());
        String address = clean(req.getAddress());
        String inviteToken = clean(req.getInviteToken());
        String password = req.getPassword();
        String confirm = req.getConfirmPassword();

        if (!StringUtils.hasText(email)) throw new IllegalArgumentException("Email required");
        if (!StringUtils.hasText(phone)) throw new IllegalArgumentException("Phone required");
        if (!StringUtils.hasText(password)) throw new IllegalArgumentException("Password required");
        if (!password.equals(confirm)) throw new IllegalArgumentException("Password mismatch");
        if (!StringUtils.hasText(inviteToken)) throw new IllegalArgumentException("Invitation token missing");

        if (repo.existsByEmailIgnoreCase(email)) throw new IllegalArgumentException("Email already exists");
        if (repo.existsByPhone(phone)) throw new IllegalArgumentException("Phone already exists");

        InvitationToken invite = invitationService.validateAndConsume(inviteToken);


        AdminUser user = AdminUser.builder()
                .fullName(fullName)
                .phone(phone)
                .email(email)
                .role(invite.getRole())
                .passwordHash(encoder.encode(password))
                .address(address)
                .enabled(true)
                .accountNonLocked(true)
                .failedAttempts(0)
                .createdAt(Instant.now())
                .build();

        return repo.save(user);
    }

    public List<AdminSearchResponse> search(String q) {
        String s = clean(q);

        if (!StringUtils.hasText(s)) {
            return repo.findAll().stream().map(this::toDto).toList();
        }

        Map<Long, AdminUser> uniq = new LinkedHashMap<>();

        for (AdminUser u : repo.findByFullNameContainingIgnoreCase(s)) putUniq(uniq, u);
        for (AdminUser u : repo.findByEmailContainingIgnoreCase(s)) putUniq(uniq, u);
        for (AdminUser u : repo.findByPhoneContaining(s)) putUniq(uniq, u);

        return uniq.values().stream().map(this::toDto).toList();
    }

    @Transactional
    public AdminUser updateProfile(Long id, UpdateProfileRequest req, MultipartFile photo) {
        if (id == null) throw new IllegalArgumentException("Invalid id");

        AdminUser u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));

        if (req != null) {
            String fullName = clean(req.getFullName());
            String address = clean(req.getAddress());

            if (StringUtils.hasText(fullName)) u.setFullName(fullName);
            if (req.getAddress() != null) u.setAddress(address);
        }



        return repo.save(u);
    }

    private AdminSearchResponse toDto(AdminUser u) {
        String url = u.getProfilePhotoPath() == null ? null : ("/uploads/" + u.getProfilePhotoPath());
        return AdminSearchResponse.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .phone(u.getPhone())
                .email(u.getEmail())
                .role(u.getRole())
                .profilePhotoUrl(url)
                .enabled(u.isEnabled())
                .accountNonLocked(u.isAccountNonLocked())
                .build();
    }

    public long totalStock() { return stockRepo.count(); }
    public long totalDelivered() { return deliveredRepo.count(); }

    public long totalTeam() { return teamRepo.count(); }

    public @Nullable Object totalStockLegacy() {
        return totalStock();
    }

    private static void putUniq(Map<Long, AdminUser> uniq, AdminUser u) {
        if (u != null && u.getId() != null) uniq.put(u.getId(), u);
    }

    private static String clean(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isBlank() ? null : t;
    }

    private static String cleanLower(String s) {
        String t = clean(s);
        return t == null ? null : t.toLowerCase(Locale.ROOT);
    }
}
