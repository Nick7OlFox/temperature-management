package com.exercise.tempManager.exceptions;

import com.exercise.tempManager.response.PartialSuccessMessage;
import lombok.Data;

@Data
public class IncompleteProcessException extends RuntimeException {

    PartialSuccessMessage<?> response;

    public IncompleteProcessException(String message, PartialSuccessMessage<?> response) {
        super(message);
        this.response = response;
    }
}
