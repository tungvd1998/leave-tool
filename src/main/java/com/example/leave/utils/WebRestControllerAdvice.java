package com.example.leave.utils;

import com.example.leave.infrastructure.constant.ErrorCode;
import com.example.leave.infrastructure.exception.DataNotFoundException;
import com.example.leave.infrastructure.exception.ExceptionResponse;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tungvd
 */

@RestControllerAdvice
@Slf4j
public class WebRestControllerAdvice {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> dataNotFoundException(final DataNotFoundException ex, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setCallerUrl(request.getRequestURI());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> internalServerError(Exception ex, final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setCallerUrl(request.getRequestURI());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
