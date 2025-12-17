package com.workspark.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Collections;
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
public class BasePageRes<T extends Serializable> implements Serializable {
    private List<T> items;
    private long pageNo;
    private long pageSize;
    private long totalPages;
    private long totalCount;
    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String error;

    /**
     * Create a success response.
     *
     * @param data          The data to include in the response.
     * @param message       The message to include in the response.
     * @param httpStatus    HTTP status for the response.
     * @param <T>           Type of the data.
     *
     * @return ResponseEntity with success BaseRes.
     */
    public static <T extends Serializable> ResponseEntity<BasePageRes<T>> success(List<T> data, String message, HttpStatus httpStatus) {
        BasePageRes<T> response = BasePageRes.<T>builder()
                .items(data)
                .success(true)
                .message(message.isBlank() ? "Request Successful" : message)
                .error(null)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }

    /**
     * Create an error response.
     *
     * @param message       The error message to include.
     * @param error         error details if available.
     * @param httpStatus    HTTP status for the response.
     * @param <T>           Type of the data.
     *
     * @return ResponseEntity with error BaseRes.
     */
    public static <T extends Serializable> ResponseEntity<BasePageRes<T>> error(
            String message,
            String error,
            HttpStatus httpStatus
    ) {
        BasePageRes<T> response = BasePageRes.<T>builder()
                .items(Collections.emptyList())
                .success(false)
                .message(message)
                .error(error.isBlank() ? "Unknown Exception" : error)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }
}
