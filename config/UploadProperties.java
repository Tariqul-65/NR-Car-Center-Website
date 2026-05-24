package com.example.nrcarcenter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.upload")
public class UploadProperties {
    private String dir;
    private String publicPrefix;

    public Path getUploadDirPath() {
        return Path.of(dir);
    }
}
