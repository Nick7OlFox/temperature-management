package com.exercise.tempManager.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessMessage<T> {
    private int status;
    private Date timestamp;
    private T data;
}
