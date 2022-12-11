package com.thoughtworks.travel.controller.handler;

import com.thoughtworks.travel.controller.dto.ApiResponse;
import com.thoughtworks.travel.exception.ErrorCodeException;
import com.thoughtworks.travel.exception.ErrorException;
import com.thoughtworks.travel.infrastructure.enums.ErrorCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.thoughtworks.travel.infrastructure.enums.ErrorCode.FAILED;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ErrorCodeException.class)
    public ApiResponse<?> handleErrorCodeException(ErrorCodeException e) {
        log.error("bad request exception", e);
        return ApiResponse.error(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ErrorException.class)
    public ApiResponse<?> handleErrorException(ErrorException e) {
        log.error("bad request exception", e);
        return ApiResponse.error(e.getErrorCode(),e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleException(Exception e) {
        log.error("internal server error", e);
        return ApiResponse.error(FAILED);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        var errorMessage = e.contentUTF8();
        log.error("feign client error: {}", errorMessage);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (HttpStatus.BAD_REQUEST.value() == e.status()) {
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.BAD_REQUEST);
        } else if (HttpStatus.NOT_FOUND.value() == e.status()) {
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.NOT_FOUND);
        } else if (HttpStatus.REQUEST_TIMEOUT.value() == e.status()) {
            return new ResponseEntity<>(ErrorCode.REQUEST_THIRD_SYSTEM_TIMEOUT.getMessage(), headers, HttpStatus.REQUEST_TIMEOUT);
        } else {
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
