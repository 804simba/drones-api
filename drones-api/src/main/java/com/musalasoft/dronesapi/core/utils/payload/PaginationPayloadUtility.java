package com.musalasoft.dronesapi.core.utils.payload;

import com.musalasoft.dronesapi.model.payload.response.PagedResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class PaginationPayloadUtility {

    public static <T> PagedResponse<T> resolvePaginationMetaData(Page<T> page, int pageNumber, int pageSize, String status, int statusCode, String message) {
        PagedResponse.PagedData<T> pagedData = new PagedResponse.PagedData<>(page.getContent(),
                new PagedResponse.PageMetadata(pageNumber, pageSize, page.getTotalPages(), page.getTotalElements()));
        return new PagedResponse<>(status, statusCode, message, LocalDateTime.now(), pagedData);
    }
}
