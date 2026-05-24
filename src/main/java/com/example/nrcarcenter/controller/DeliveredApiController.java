package com.example.nrcarcenter.controller;

import com.example.nrcarcenter.entity.DeliveredCar;
import com.example.nrcarcenter.service.DeliveredService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/delivered")
public class DeliveredApiController {

    private final DeliveredService service;

    @GetMapping
    public List<DeliveredCar> list(@RequestParam(required = false) String q) {
        return service.list(q);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DeliveredCar create(@ModelAttribute DeliveredCar payload,
                               @RequestParam(name = "image", required = false) MultipartFile image) {
        return service.create(payload, image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DeliveredCar update(@PathVariable Long id,
                               @ModelAttribute DeliveredCar payload,
                               @RequestParam(name = "image", required = false) MultipartFile image) {
        return service.update(id, payload, image);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
