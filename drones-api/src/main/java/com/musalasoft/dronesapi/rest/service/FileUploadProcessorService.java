package com.musalasoft.dronesapi.rest.service;

import com.musalasoft.dronesapi.model.payload.base.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadProcessorService {
    BaseResponse<?> handleFileUploadRequest(MultipartFile multipartFile);
}
