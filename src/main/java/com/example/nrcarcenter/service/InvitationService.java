package com.example.nrcarcenter.service;


import com.example.nrcarcenter.entity.InvitationToken;
import com.example.nrcarcenter.entity.Role;
import com.example.nrcarcenter.repository.InvitationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationTokenRepository repo;
    private final SecureRandom rnd = new SecureRandom();

    public InvitationToken create(Role role, int validHours) {
        byte[] b = new byte[24];
        rnd.nextBytes(b);
        String token = HexFormat.of().formatHex(b);

        InvitationToken it = InvitationToken.builder()
                .token(token)
                .role(role)
                .expiresAt(Instant.now().plus(validHours, ChronoUnit.HOURS))
                .build();

        return repo.save(it);
    }

    public InvitationToken validateAndConsume(String token) {
        InvitationToken it = repo.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid invite"));
        if (it.isUsed()) throw new IllegalArgumentException("Invite used");
        if (Instant.now().isAfter(it.getExpiresAt())) throw new IllegalArgumentException("Invite expired");
        it.setUsed(true);
        it.setUsedAt(Instant.now());
        return repo.save(it);
    }
}
