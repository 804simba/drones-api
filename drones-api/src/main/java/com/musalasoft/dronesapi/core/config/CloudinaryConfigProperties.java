package com.musalasoft.dronesapi.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "application.cloudinary")
public class CloudinaryConfigProperties {

    private String cloudName;

    private String apiKey;

    private String apiSecret;
}
