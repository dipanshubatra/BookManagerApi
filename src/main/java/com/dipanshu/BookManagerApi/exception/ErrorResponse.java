package com.dipanshu.BookManagerApi.exception;


import java.time.LocalDateTime;

public class ErrorResponse {
    private boolean success;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    public ErrorResponse(){}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ErrorResponse(boolean success, String message, String errorCode, LocalDateTime timestamp){
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }
}
