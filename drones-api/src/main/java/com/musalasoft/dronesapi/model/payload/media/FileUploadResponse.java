package com.musalasoft.dronesapi.model.payload.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadResponse {

    @JsonProperty("media_id")
    private Long mediaId;

    @JsonProperty("image_url")
    private String url;

    @JsonProperty("media_filename")
    private String mediaName;

    @JsonProperty("cloudinary_public_id")
    private String publicId;

    @JsonProperty("media_type")
    private String mediaType;
}
