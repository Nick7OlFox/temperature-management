package com.exercise.tempManager.exceptions;

import lombok.Data;

import java.util.List;

@Data
public class BulkProcessingFailureException extends RuntimeException {
    private List<String> errors;

    public BulkProcessingFailureException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

}
