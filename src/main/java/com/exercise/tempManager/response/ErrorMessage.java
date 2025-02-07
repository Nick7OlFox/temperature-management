package com.exercise.tempManager.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    /**
     * Constructor to be used when creating the ErrorMessage through the @RestControllerExceptionHandler
     * @param statusCode The HTTP status code
     * @param ex The exception that was caught
     * @param request The request that was caight by the handler
     */
    public ErrorMessage(int statusCode, Exception ex, WebRequest request){
        this.statusCode = statusCode;
        this.timestamp = new Date();
        this.message = ex.getMessage();
        this.description = request.getDescription(false);
    }
}
