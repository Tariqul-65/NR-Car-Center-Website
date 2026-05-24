package com.example.nrcarcenter.controller;

import com.example.nrcarcenter.entity.StockCar;
import com.example.nrcarcenter.entity.StockStatus;
import com.example.nrcarcenter.service.DeliveredService;
import com.example.nrcarcenter.service.StockService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final StockService stockService;
    private final DeliveredService deliveredService;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @GetMapping("/")
    public String home(Model model) {
        List<StockCar> stocks =
                stockService.list(null, StockStatus.IN_STOCK)
                        .stream()
                        .limit(6)
                        .toList();

        model.addAttribute("stocks", stocks);
        return "nrindex";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping({"/delivered", "/delivered.html"})
    public String delivered(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("deliveredCars", deliveredService.list(q));
        model.addAttribute("q", q == null ? "" : q);
        return "delivered";
    }

    @GetMapping("/stock-list")
    public String stockList(@RequestParam(required = false) String q, Model model) {
        List<StockCar> rows = stockService.list(q, null);

        model.addAttribute("rows", rows);
        model.addAttribute("total", rows == null ? 0 : rows.size());
        model.addAttribute("q", q == null ? "" : q);

        return "stock-list";
    }

    @GetMapping("/car/{id}")
    public String carDetails(@PathVariable Long id, Model model) {
        StockCar car = stockService.getById(id);
        model.addAttribute("car", car);
        model.addAttribute("images", car.getImages());
        return "car";
    }


    @GetMapping("/car/{id}/zip")
    public void downloadCarImagesZip(@PathVariable Long id, HttpServletResponse response) throws IOException {

        StockCar car = stockService.getById(id);
        if (car == null || car.getImages() == null || car.getImages().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/zip");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=car-" + safeFileName(car.getStockId()) + "-images.zip"
        );

        Path baseDir = Paths.get(uploadDir).toAbsolutePath().normalize();

        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            int idx = 1;

            for (var img : car.getImages()) {
                if (img == null || img.getImagePath() == null || img.getImagePath().isBlank()) continue;

                Path file = baseDir.resolve(img.getImagePath()).normalize();


                if (!file.startsWith(baseDir)) continue;
                if (!Files.exists(file) || Files.isDirectory(file)) continue;

                String ext = getExt(file.getFileName().toString());
                String entryName = safeFileName(car.getStockId()) + "-" + idx + ext;

                zos.putNextEntry(new ZipEntry(entryName));
                Files.copy(file, zos);
                zos.closeEntry();

                idx++;
            }

            zos.finish();
        }
    }

    private static String safeFileName(String s) {
        if (s == null) return "car";
        return s.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private static String getExt(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0) return "";
        return filename.substring(dot);
    }
}
