package com.parkspot.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/** Simple JSON body returned for every error. "errors" is only present for validation failures. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(LocalDateTime timestamp, int status, String message, Map<String, String> errors) {

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(LocalDateTime.now().withNano(0), status, message, null);
    }

    public static ErrorResponse of(int status, String message, Map<String, String> errors) {
        return new ErrorResponse(LocalDateTime.now().withNano(0), status, message, errors);
    }
}
