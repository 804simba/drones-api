package com.musalasoft.dronesapi.rest.service.impl;

import com.musalasoft.dronesapi.core.exception.FileUploadException;
import com.musalasoft.dronesapi.core.utils.Constants;
import com.musalasoft.dronesapi.core.utils.media.FilesUtils;
import com.musalasoft.dronesapi.model.entity.Media;
import com.musalasoft.dronesapi.model.payload.base.BaseResponse;
import com.musalasoft.dronesapi.model.payload.media.FileUploadResponse;
import com.musalasoft.dronesapi.model.repository.MediaRepository;
import com.musalasoft.dronesapi.rest.service.CloudinaryFileUploadService;
import com.musalasoft.dronesapi.rest.service.FileUploadProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class FileUploadProcessorServiceImpl implements FileUploadProcessorService {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_UPLOAD_FILE_SIZE;

    @Autowired
    private CloudinaryFileUploadService cloudinaryFileUploadService;

    @Autowired
    private MediaRepository mediaRepository;

    @Override
    public BaseResponse<?> handleFileUploadRequest(MultipartFile multipartFile) {
        if (!ObjectUtils.isEmpty(multipartFile)) {
            if (!ObjectUtils.isEmpty(multipartFile.getContentType())) {
                if (multipartFile.getContentType().equalsIgnoreCase(MediaType.IMAGE_PNG_VALUE) || multipartFile.getContentType().equalsIgnoreCase(MediaType.IMAGE_JPEG_VALUE)
                        || multipartFile.getContentType().equalsIgnoreCase(MediaType.IMAGE_GIF_VALUE) || multipartFile.getContentType().equalsIgnoreCase("image/jpg")) {
                    double fileSize = FilesUtils.resolveFileSizeInMegaBytes(multipartFile.getSize());
                    double applicationMaxFileSize = FilesUtils.parseMaxFileSize(MAX_UPLOAD_FILE_SIZE);
                    if (fileSize <= applicationMaxFileSize) {
                        Media uploadedMedia = cloudinaryFileUploadService.handleFileUpload(multipartFile);
                        mediaRepository.save(uploadedMedia);
                        FilesUtils.deleteFileUpload(multipartFile);
                        FileUploadResponse fileUploadResponse = FileUploadResponse.builder().mediaName(uploadedMedia.getFileName()).mediaId(uploadedMedia.getId())
                                .url(uploadedMedia.getUrl()).mediaType(uploadedMedia.getMediaType()).publicId(uploadedMedia.getPublicId()).build();
                        return BaseResponse.builder().responseCode(Constants.ResponseStatusCode.SUCCESS)
                                .responseMessage("file upload successful").responseTime(LocalDateTime.now())
                                .data(fileUploadResponse).build();
                    } else {
                        FilesUtils.deleteFileUpload(multipartFile);
                        throw new FileUploadException("file size exceeds maximum upload size");
                    }
                }
            } else {
                throw new FileUploadException("file upload failed");
            }
        }
        throw new FileUploadException("file upload failed");
    }
}
