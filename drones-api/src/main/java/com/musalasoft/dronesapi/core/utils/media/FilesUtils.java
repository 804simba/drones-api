package com.musalasoft.dronesapi.core.utils.media;

import com.musalasoft.dronesapi.core.exception.FileUploadException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

public class FilesUtils {
    public static double parseMaxFileSize(String maxUploadFileSize) {
        if (ObjectUtils.isEmpty(maxUploadFileSize)) {
            throw new FileUploadException("max upload file size cannot be null");
        }
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
