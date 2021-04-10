package com.example.leave.api.view;

import com.example.leave.infrastructure.constant.ErrorCode;
import com.example.leave.infrastructure.exception.DataNotFoundException;
import com.example.leave.utils.ExceptionConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author tungvd
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseObject {
    private boolean isSuccess = false;
    private Object data;
    private String message;

    public ResponseObject() {

    }

    public <T> ResponseObject(T obj) {
        if (obj != null) {
            this.isSuccess = true;
            this.data = obj;
            this.message = "success";
        }
    }

    public ResponseObject(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }
}
