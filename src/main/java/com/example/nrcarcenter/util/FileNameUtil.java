package com.example.nrcarcenter.util;

import java.util.UUID;

public final class FileNameUtil {
    private FileNameUtil() {}

    public static String randomName(String original) {
        String ext = "";
        int i = original == null ? -1 : original.lastIndexOf('.');
        if (i > -1 && i < original.length() - 1) ext = original.substring(i).toLowerCase();
        return UUID.randomUUID() + ext;
    }
}
