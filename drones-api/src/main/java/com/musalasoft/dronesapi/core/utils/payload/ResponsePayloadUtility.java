package com.musalasoft.dronesapi.core.utils.payload;

import com.musalasoft.dronesapi.core.utils.Constants;
import com.musalasoft.dronesapi.model.payload.base.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

public class ResponsePayloadUtility {
    public static BaseResponse<?> createSuccessResponse(Object data, String... responseMessage) {
        return new BaseResponse<>(HttpStatus.OK.value(), (!ObjectUtils.isEmpty(responseMessage) ? responseMessage[0] : Constants.ResponseMessages.SUCCESS),
                LocalDateTime.now(), data);
    }

    public static BaseResponse<?> createFailedResponse(String... responseMessage) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), (!ObjectUtils.isEmpty(responseMessage) ? responseMessage[0] : Constants.ResponseMessages.FAILED),
                LocalDateTime.now());
    }
}
