package com.exercise.tempManager.advice;

import com.exercise.tempManager.exceptions.DeviceNotFoundException;
import com.exercise.tempManager.exceptions.RecordNotFoundException;
import com.exercise.tempManager.response.ErrorMessage;
import com.exercise.tempManager.response.SuccessMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {

    /**
     * Advice for when the device does not have any records at the specific time
     * @param ex Exception caught
     */
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void recordNotFound(Exception ex, WebRequest request){
        log.warn("No content found for request: " + request.getDescription(false) + " (" + ex.getMessage()+")");
    }

    /**
     * Advice for when the device was not found
     * @param ex Exception caught
     */
    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deviceNotFound(Exception ex, WebRequest request){
        log.warn("No content found for request: " + request.getDescription(false)  + " (" + ex.getMessage()+")");
    }

    /**
     * Global uncaught exception handler
     * @param ex Exception caught
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex){
        log.warn(ex.getMessage());
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex);
    }

}
