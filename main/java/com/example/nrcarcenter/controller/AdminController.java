package com.example.nrcarcenter.controller;

import com.example.nrcarcenter.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminUserService adminUserService;

    @GetMapping({ "", "/", "/index" })
    public String dashboard(Model model) {
        model.addAttribute("stockCount", adminUserService.totalStock());
        model.addAttribute("deliveredCount", adminUserService.totalDelivered());
        model.addAttribute("teamCount", adminUserService.totalTeam());
        return "index";
    }
}
