package com.musalasoft.dronesapi.core.utils.media;

import org.springframework.web.multipart.MultipartFile;

public class FilesUtils {
    public static double parseMaxFileSize(String maxUploadFileSize) {
        String numericValue = maxUploadFileSize.replaceAll("[^\\d.]", "");
        return Double.parseDouble(numericValue);
    }

    public static void deleteFileUpload(MultipartFile multipartFile) {
        try {
            if (multipartFile.getResource().isFile()) {
                multipartFile.getResource().getFile().delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double resolveFileSizeInMegaBytes(long fileSize) {
        return (double) fileSize / (1024 * 1024);
    }
}
