package com.example.nrcarcenter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public String saveImage(MultipartFile file, String prefix) {
        if (file == null || file.isEmpty()) return null;

        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String ext = extractExt(original);
        if (ext == null) ext = "jpg";

        String name = (prefix == null ? "img" : prefix) + "_" + UUID.randomUUID().toString().replace("-", "") + "." + ext;

        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            Path target = dir.resolve(name).normalize();
            if (!target.startsWith(dir)) throw new IllegalArgumentException("Invalid path");

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            trySetReadOnly(target);

            return name;
        } catch (IOException e) {
            throw new IllegalStateException("File save failed");
        }
    }

    private static void trySetReadOnly(Path p) {
        try {
            p.toFile().setReadable(true, false);
            p.toFile().setWritable(false, false);
        } catch (Exception ignored) {
        }
    }

    private static String extractExt(String filename) {
        String f = filename.toLowerCase(Locale.ROOT);
        int dot = f.lastIndexOf('.');
        if (dot < 0 || dot == f.length() - 1) return null;
        String ext = f.substring(dot + 1);
        if (ext.length() > 8) return null;
        if (!ext.matches("[a-z0-9]+")) return null;
        return ext;
    }
}
