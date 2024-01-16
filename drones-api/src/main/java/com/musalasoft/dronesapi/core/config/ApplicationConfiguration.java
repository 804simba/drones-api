package com.musalasoft.dronesapi.core.config;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final CloudinaryConfigProperties cloudinaryConfigProperties;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> cloudinaryConfigMap = new HashMap<>();
        cloudinaryConfigMap.put("cloud_name", cloudinaryConfigProperties.getCloudName());
        cloudinaryConfigMap.put("api_key", cloudinaryConfigProperties.getApiKey());
        cloudinaryConfigMap.put("api_secret", cloudinaryConfigProperties.getApiSecret());
        return new Cloudinary(cloudinaryConfigMap);
    }
}
