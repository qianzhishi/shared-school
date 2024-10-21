package com.sharedschool.backend.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    private ApiResponse(int code) {
        this.code = code;
    }
    private ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }
    public static <T> ApiResponse<T> fail() {
        return new ApiResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
    }
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(ResponseCode.ERROR.getCode(), message);
    }
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message);
    }



}

