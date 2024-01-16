package com.musalasoft.dronesapi.rest.service.impl;

import com.musalasoft.dronesapi.core.exception.FileUploadException;
import com.musalasoft.dronesapi.core.utils.Constants;
import com.musalasoft.dronesapi.model.entity.Media;
import com.musalasoft.dronesapi.model.payload.base.BaseResponse;
import com.musalasoft.dronesapi.model.repository.MediaRepository;
import com.musalasoft.dronesapi.rest.service.CloudinaryFileUploadService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUploadProcessorServiceImplTest {

    @Mock
    private CloudinaryFileUploadService cloudinaryFileUploadService;

    @Mock
    private MediaRepository mediaRepository;

    @InjectMocks
    private FileUploadProcessorServiceImpl fileUploadProcessorService;

    @Test
    void handleTestFileUploadRequestForThrowsExceptionForInvalidMaxFileSize() throws IOException {
        MultipartFile mockMultipartFile = createMockMultipartFile("image/jpg", 1024);
        assertThrows(FileUploadException.class, () -> fileUploadProcessorService.handleFileUploadRequest(mockMultipartFile));
    }

    @Test
    void handleTestFileUploadRequestForSuccessfulUpload() throws Exception {
        MultipartFile mockMultipartFile = createMockMultipartFile("image/jpg", 1024);
        Media mockMedia = createMockMedia();
        when(cloudinaryFileUploadService.handleFileUpload(mockMultipartFile)).thenReturn(mockMedia);

        Field maxUploadFileSizeField = FileUploadProcessorServiceImpl.class.getDeclaredField("MAX_UPLOAD_FILE_SIZE");
        maxUploadFileSizeField.setAccessible(true);
        maxUploadFileSizeField.set(fileUploadProcessorService, "5MB");

        BaseResponse<?> result = fileUploadProcessorService.handleFileUploadRequest(mockMultipartFile);

        assertNotNull(result);
        assertEquals(Constants.ResponseStatusCode.SUCCESS, result.getResponseCode());
        assertEquals("file upload successful", result.getResponseMessage());
        assertNotNull(result.getData());

        verify(mediaRepository, times(1)).save(mockMedia);
        verify(cloudinaryFileUploadService, times(1)).handleFileUpload(mockMultipartFile);
        verifyNoMoreInteractions(mediaRepository, cloudinaryFileUploadService);
    }

    @Test
    void handleFileUploadRequestInvalidFileType() throws IOException {
        MultipartFile mockMultipartFile = createMockMultipartFile("text/plain", 1024);
        FileUploadException exception = assertThrows(FileUploadException.class,
                () -> fileUploadProcessorService.handleFileUploadRequest(mockMultipartFile));
        assertEquals("file upload failed", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void handleFileUploadRequest_ExceedsFileSizeLimit() {
        MultipartFile mockMultipartFile = createMockMultipartFile("image/jpg", 10 * 1024 * 1024);

        Field maxUploadFileSizeField = FileUploadProcessorServiceImpl.class.getDeclaredField("MAX_UPLOAD_FILE_SIZE");
        maxUploadFileSizeField.setAccessible(true);
        maxUploadFileSizeField.set(fileUploadProcessorService, "5MB");
        FileUploadException exception = assertThrows(FileUploadException.class,
                () -> fileUploadProcessorService.handleFileUploadRequest(mockMultipartFile));
        assertEquals("file size exceeds maximum upload size", exception.getMessage());
    }

    private MultipartFile createMockMultipartFile(String contentType, long fileSize) throws IOException {
        return new MockMultipartFile("file", "test.jpg", contentType, new byte[(int) fileSize]);
    }

    private Media createMockMedia() {
        Media media = new Media();
        media.setId(1L);
        media.setFileName("test.jpg");
        media.setUrl("https://example.com/test.jpg");
        media.setPublicId("public_123333");
        return media;
    }
}