package com.thoughtworks.enterprise.travel.controller.handler;

import com.thoughtworks.enterprise.travel.controller.dto.ApiResponse;
import com.thoughtworks.enterprise.travel.exception.ErrorCodeException;
import com.thoughtworks.enterprise.travel.exception.ErrorException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.thoughtworks.enterprise.travel.infrastructure.enums.ErrorCode.FAILED;
import static com.thoughtworks.enterprise.travel.infrastructure.enums.ErrorCode.REQUEST_THIRD_SYSTEM_ERROR;

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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleFeignException(FeignException e) {
        var errorMessage = e.contentUTF8();
        log.error("feign client error: {}", errorMessage);
        return ApiResponse.error(REQUEST_THIRD_SYSTEM_ERROR);
    }
}
