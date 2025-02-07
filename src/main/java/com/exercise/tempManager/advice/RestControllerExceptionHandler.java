package com.exercise.tempManager.advice;

import com.exercise.tempManager.exceptions.BulkProcessingFailureException;
import com.exercise.tempManager.exceptions.DeviceNotFoundException;
import com.exercise.tempManager.exceptions.IncompleteProcessException;
import com.exercise.tempManager.exceptions.RecordNotFoundException;
import com.exercise.tempManager.response.ErrorMessage;
import com.exercise.tempManager.response.PartialSuccessMessage;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
@Hidden
public class RestControllerExceptionHandler {

    /**
     * Advice for when the device does not have any records at the specific time
     *
     * @param ex Exception caught
     */
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void recordNotFound(Exception ex, WebRequest request) {
        log.warn("No content found for request: " + request.getDescription(false) + " (" + ex.getMessage() + ")");
    }

    /**
     * Advice for when the device was not found
     *
     * @param ex Exception caught
     */
    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deviceNotFound(Exception ex, WebRequest request) {
        log.warn("No content found for request: " + request.getDescription(false) + " (" + ex.getMessage() + ")");
    }

    /**
     * Advice for when a batch processing partially fails
     *
     * @param ex Exception caught
     */
    @ExceptionHandler(IncompleteProcessException.class)
    @ResponseStatus(value = HttpStatus.MULTI_STATUS)
    public PartialSuccessMessage<?> incompleteProcessException(IncompleteProcessException ex, WebRequest request) {
        log.warn("Partial success processing request: " + request.getDescription(false));
        return ex.getResponse();
    }

    /**
     * Advice for when a batch processing all fails
     *
     * @param ex Exception caught
     */
    @ExceptionHandler(BulkProcessingFailureException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage bulkProcessingFailureException(BulkProcessingFailureException ex, WebRequest request) {
        log.warn("Complete failure processing request: " + request.getDescription(false));
        ErrorMessage response = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex);
        response.setMultiErrorList(ex.getErrors());
        return response;
    }

    /**
     * When an invalid parameter is sent
     *
     * @param ex Exception caught
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage illegalArgumentException(Exception ex) {
        log.warn(ex.getMessage());
        return new ErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex);
    }

    /**
     * Global uncaught exception handler
     *
     * @param ex Exception caught
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex) {
        log.warn(ex.getMessage());
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex);
    }

}
