package com.workspark.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Base response class for all API responses.
 *
 * @param <T>   Type of the item in the response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedRes<T extends Serializable> implements Serializable {
    private List<T> data;
    private long pageNo;
    private long pageSize;
    private long totalPages;
    private long totalCount;
}
