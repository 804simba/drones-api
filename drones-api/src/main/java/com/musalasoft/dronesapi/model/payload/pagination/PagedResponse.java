package com.musalasoft.dronesapi.model.payload.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.musalasoft.dronesapi.core.utils.time.PayloadLocalDateTimeDeserializer;
import com.musalasoft.dronesapi.core.utils.time.PayloadLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    @JsonProperty("status")
    private String status;

    @JsonProperty("status_code")
    private int code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("response_time")
    @JsonSerialize(using = PayloadLocalDateTimeSerializer.class)
    @JsonDeserialize(using = PayloadLocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;

    @JsonProperty("data")
    private PagedData<T> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedData<T> {
        @JsonProperty("content")
        private List<T> content;

        @JsonProperty("metadata")
        private PageMetadata metadata;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageMetadata {

        @JsonProperty("page_number")
        private int pageNumber;

        @JsonProperty("page_size")
        private int pageSize;

        @JsonProperty("total_pages")
        private int totalPages;

        @JsonProperty("total_elements")
        private long totalElements;
    }
}
