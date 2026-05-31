package com.example.ordermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for paginated response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> {

    private java.util.List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
}

