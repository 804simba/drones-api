package com.musalasoft.dronesapi.model.payload.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.musalasoft.dronesapi.core.utils.time.PayloadLocalDateTimeDeserializer;
import com.musalasoft.dronesapi.core.utils.time.PayloadLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {
    @JsonProperty("responseCode")
    private int responseCode;

    @JsonProperty("responseMessage")
    private String responseMessage;

    @JsonProperty("response_time")
    @JsonSerialize(using = PayloadLocalDateTimeSerializer.class)
    @JsonDeserialize(using = PayloadLocalDateTimeDeserializer.class)
    private LocalDateTime responseTime;

    @JsonProperty("data")
    private T data;

    public BaseResponse(int responseCode, String responseMessage, LocalDateTime responseTime) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseTime = responseTime;
    }
}
