package com.example.nrcarcenter.config;

import com.example.nrcarcenter.entity.AdminUser;
import com.example.nrcarcenter.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AdminUserRepository adminRepo;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            AdminUser u = adminRepo.findByEmailIgnoreCase(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!u.isEnabled() || !u.isAccountNonLocked()) {
                throw new BadCredentialsException("Access denied");
            }

            return new User(
                    u.getEmail(),
                    u.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/admin/api/**"))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/about",
                                "/contact",
                                "/delivered",
                                "/stock-list",
                                "/car/*"          // ✅ added: allow car details page publicly
                        ).permitAll()

                        .requestMatchers("/auth/login", "/auth/register", "/auth/register/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers(
                                "/favicon.ico",
                                "/uploads/**",
                                "/**/*.css",
                                "/**/*.js",
                                "/**/*.png",
                                "/**/*.jpg",
                                "/**/*.jpeg",
                                "/**/*.webp",
                                "/**/*.svg",
                                "/logo.jpeg",
                                "/footer-ad.jpg",
                                "/weaccept-amex.png",
                                "/weaccept-visa.png",
                                "/weaccept-master.png",
                                "/weaccept-bkash.png"
                        ).permitAll()

                        .requestMatchers("/admin/invites/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/admin/**").authenticated()
                        .requestMatchers("/api/**").authenticated()

                        .requestMatchers("/car/*/zip").permitAll()

                        .anyRequest().permitAll()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> {
                            String uri = req.getRequestURI();
                            if (uri.startsWith("/admin/api/") || uri.startsWith("/api/")) {
                                res.sendError(401);
                                return;
                            }
                            res.sendRedirect("/auth/login");
                        })
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/admin/index", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }
}
