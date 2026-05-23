package com.example.nrcarcenter.controller;

import com.example.nrcarcenter.entity.StockCar;
import com.example.nrcarcenter.entity.StockStatus;
import com.example.nrcarcenter.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/stock")
public class StockApiController {

    private final StockService service;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("images");
    }

    @GetMapping
    public List<StockCar> list(@RequestParam(required = false) String q,
                               @RequestParam(required = false) StockStatus status) {
        return service.list(q, status);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StockCar create(@ModelAttribute StockCar payload,
                           @RequestParam(name = "images", required = false) List<MultipartFile> images) {
        return service.create(payload, images);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StockCar update(@PathVariable Long id,
                           @ModelAttribute StockCar payload,
                           @RequestParam(name = "images", required = false) List<MultipartFile> images) {
        return service.update(id, payload, images);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
