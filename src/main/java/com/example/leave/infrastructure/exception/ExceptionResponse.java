package com.example.leave.infrastructure.exception;

import java.util.List;

/**
 * @author tungvd
 */
public class ExceptionResponse<T> {
    private String message;
    private String callerUrl;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallerUrl() {
        return callerUrl;
    }

    public void setCallerUrl(String callerUrl) {
        this.callerUrl = callerUrl;
    }

}
