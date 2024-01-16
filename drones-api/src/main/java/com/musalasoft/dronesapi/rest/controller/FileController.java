package com.musalasoft.dronesapi.rest.controller;

import com.musalasoft.dronesapi.model.payload.base.BaseResponse;
import com.musalasoft.dronesapi.rest.service.FileUploadProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/files")
public class FileController {

    private final FileUploadProcessorService fileUploadProcessorService;

    @Autowired
    public FileController(FileUploadProcessorService fileUploadProcessorService) {
        this.fileUploadProcessorService = fileUploadProcessorService;
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResponse<?> handleUploadFile(@RequestParam("file") MultipartFile file) {
        return fileUploadProcessorService.handleFileUploadRequest(file);
    }
}
