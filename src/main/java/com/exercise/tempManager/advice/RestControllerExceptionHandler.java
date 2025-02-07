package com.exercise.tempManager.advice;

import com.exercise.tempManager.exceptions.DeviceNotFoundException;
import com.exercise.tempManager.response.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {

    // TODO ensure this one is needed
    /**
     * Exception handler for uncaught DeviceNotFoundException
     * @param ex DeviceNotFoundException caught
     * @param request Request made
     */
    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage deviceNotFoundException(DeviceNotFoundException ex, WebRequest request){
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex, request);
    }

    /**
     * Global uncaught exception handler
     * @param ex Exception caught
     * @param request Request made
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request){
        log.warn(ex.getMessage());
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex,request);
    }

}
