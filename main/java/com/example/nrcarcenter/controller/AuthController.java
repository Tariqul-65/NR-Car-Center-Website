package com.example.nrcarcenter.controller;

import com.example.nrcarcenter.dto.RegisterRequest;
import com.example.nrcarcenter.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AdminUserService adminUserService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(@RequestParam(name = "token", required = false) String token, Model model) {
        if (token == null || token.isBlank()) {
            return "redirect:/auth/login";
        }

        RegisterRequest req = new RegisterRequest();
        req.setInviteToken(token);
        model.addAttribute("registerRequest", req);
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("registerRequest") RegisterRequest req,
                                 BindingResult br,
                                 @RequestParam(name = "photo", required = false) MultipartFile photo,
                                 Model model) {

        if (req.getInviteToken() == null || req.getInviteToken().isBlank()) {
            model.addAttribute("error", "Invitation token missing");
            return "register";
        }

        if (br.hasErrors()) {
            return "register";
        }

        try {
            adminUserService.register(req, photo);
            return "redirect:/auth/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
