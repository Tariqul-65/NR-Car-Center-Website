package com.example.nrcarcenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginAliasController {

    @GetMapping("/login")
    public String loginAlias() {
        return "redirect:/auth/login";
    }
}
