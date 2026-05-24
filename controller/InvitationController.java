package com.example.nrcarcenter.controller;


import com.example.nrcarcenter.dto.InviteCreateRequest;
import com.example.nrcarcenter.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/invites")
public class InvitationController {

    private final InvitationService invitationService;

    @GetMapping
    public String invites(Model model) {
        model.addAttribute("inviteCreateRequest", new InviteCreateRequest());
        return "admin/invites";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute InviteCreateRequest req, Model model) {
        try {
            var invite = invitationService.create(req.getRole(), req.getValidHours());
            model.addAttribute("token", invite.getToken());
            model.addAttribute("role", invite.getRole().name());
            model.addAttribute("expiresAt", invite.getExpiresAt().toString());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("inviteCreateRequest", new InviteCreateRequest());
        return "admin/invites";
    }
}
