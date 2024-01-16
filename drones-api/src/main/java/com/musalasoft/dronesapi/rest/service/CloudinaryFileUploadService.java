package com.musalasoft.dronesapi.rest.service;

import com.musalasoft.dronesapi.model.entity.Media;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryFileUploadService {
    Media handleFileUpload(MultipartFile multipartFile);
}
