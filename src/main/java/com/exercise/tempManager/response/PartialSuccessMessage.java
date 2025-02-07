package com.exercise.tempManager.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartialSuccessMessage<T> {
    private int status;
    private Timestamp timestamp;
    private List<T> processedEntries;
    private List<String> unprocessedEntries;
}
