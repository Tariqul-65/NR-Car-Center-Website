package com.example.nrcarcenter.service;

import com.example.nrcarcenter.entity.TeamMember;
import com.example.nrcarcenter.entity.TeamRole;
import com.example.nrcarcenter.repository.TeamMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamMemberRepository repo;
    private final FileStorageService fileStorage;

    public List<TeamMember> list(String q, TeamRole role) {
        String s = clean(q);

        if (!StringUtils.hasText(s) && role == null) return repo.findAll();
        if (!StringUtils.hasText(s)) return repo.findByRole(role);

        Map<Long, TeamMember> uniq = new LinkedHashMap<>();
        repo.findByNameContainingIgnoreCase(s).forEach(x -> putUniq(uniq, x));
        repo.findByPhoneContaining(s).forEach(x -> putUniq(uniq, x));
        repo.findByEmailContainingIgnoreCase(s).forEach(x -> putUniq(uniq, x));

        List<TeamMember> out = new ArrayList<>(uniq.values());
        if (role != null) out.removeIf(x -> x.getRole() != role);
        return out;
    }

    @Transactional
    public TeamMember create(TeamMember payload, MultipartFile photo) {
        if (payload == null) throw new IllegalArgumentException("Invalid payload");

        String phone = req(clean(payload.getPhone()), "Phone required");
        if (repo.existsByPhone(phone)) throw new IllegalArgumentException("Phone already exists");

        TeamMember m = TeamMember.builder()
                .name(req(clean(payload.getName()), "Name required"))
                .phone(phone)
                .email(clean(payload.getEmail()))
                .role(req(payload.getRole(), "Role required"))
                .status(req(payload.getStatus(), "Status required"))
                .build();

        String saved = fileStorage.saveImage(photo, "team");
        m.setPhotoPath(saved);

        return repo.save(m);
    }

    @Transactional
    public TeamMember update(Long id, TeamMember payload, MultipartFile photo) {
        if (id == null) throw new IllegalArgumentException("Invalid id");
        TeamMember m = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));

        if (payload != null) {
            setIfText(payload.getName(), m::setName);

            if (StringUtils.hasText(payload.getPhone()) && !payload.getPhone().equals(m.getPhone())) {
                String newPhone = clean(payload.getPhone());
                if (repo.existsByPhone(newPhone)) throw new IllegalArgumentException("Phone already exists");
                m.setPhone(newPhone);
            }

            m.setEmail(clean(payload.getEmail()));
            if (payload.getRole() != null) m.setRole(payload.getRole());
            if (payload.getStatus() != null) m.setStatus(payload.getStatus());
        }

        if (photo != null && !photo.isEmpty()) {
            String saved = fileStorage.saveImage(photo, "team");
            m.setPhotoPath(saved);
        }

        return repo.save(m);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("Invalid id");
        repo.deleteById(id);
    }

    private static void putUniq(Map<Long, TeamMember> uniq, TeamMember x) {
        if (x != null && x.getId() != null) uniq.put(x.getId(), x);
    }

    private static String req(String v, String msg) {
        if (!StringUtils.hasText(v)) throw new IllegalArgumentException(msg);
        return v;
    }

    private static <T> T req(T v, String msg) {
        if (v == null) throw new IllegalArgumentException(msg);
        return v;
    }

    private static void setIfText(String v, java.util.function.Consumer<String> setter) {
        String t = clean(v);
        if (StringUtils.hasText(t)) setter.accept(t);
    }

    private static String clean(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isBlank() ? null : t;
    }
}
