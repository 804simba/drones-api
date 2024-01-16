package com.musalasoft.dronesapi.rest.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.musalasoft.dronesapi.core.exception.FileUploadException;
import com.musalasoft.dronesapi.model.entity.Media;
import com.musalasoft.dronesapi.rest.service.CloudinaryFileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryFileUploadServiceImpl implements CloudinaryFileUploadService {

    private final Cloudinary cloudinary;

    @SneakyThrows
    @Override
    public Media handleFileUpload(MultipartFile multipartFile) {
        Map<?, ?> cloudinaryResponse = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.asMap(
                        "public_id", generatePublicId(),
                        "use_filename", true,
                        "timestamp", LocalDateTime.now().toString(),
                        "unique_filename", true,
                        "overwrite", true));

        if (!org.springframework.util.ObjectUtils.isEmpty(cloudinaryResponse)) {
            Media media = new Media();
            media.setFileName(cloudinaryResponse.get("original_filename").toString());
            media.setMediaType(cloudinaryResponse.get("format").toString());
            media.setUrl(cloudinaryResponse.get("url").toString());
            media.setPublicId(cloudinaryResponse.get("public_id").toString());
            return media;
        } else {
            throw new FileUploadException("file upload to cloudinary failed");
        }
    }

    private String generatePublicId() {
        return "img" + LocalDateTime.now();
    }
}
