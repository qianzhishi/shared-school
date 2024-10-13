package com.sharedschool.backend.exception;

import com.sharedschool.backend.response.ApiResponse;
import com.sharedschool.backend.response.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        return ApiResponse.fail(e.getMessage());
    }
}
