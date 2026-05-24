package com.example.nrcarcenter.controller;

import com.example.nrcarcenter.entity.TeamMember;
import com.example.nrcarcenter.entity.TeamRole;
import com.example.nrcarcenter.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/team")
public class TeamApiController {

    private final TeamService service;

    @GetMapping
    public List<TeamMember> list(@RequestParam(required = false) String q,
                                 @RequestParam(required = false) TeamRole role) {
        return service.list(q, role);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TeamMember create(@ModelAttribute TeamMember payload,
                             @RequestParam(name = "photo", required = false) MultipartFile photo) {
        return service.create(payload, photo);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TeamMember update(@PathVariable Long id,
                             @ModelAttribute TeamMember payload,
                             @RequestParam(name = "photo", required = false) MultipartFile photo) {
        return service.update(id, payload, photo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
