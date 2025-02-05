package com.exercise.tempManager.dto.compk;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The table device_records has a composite PK. This class represents that same relation
 * between the deviceName and timeOfRecording columns
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Embeddable
public class RecordId implements Serializable {
    private String deviceName;
    private Timestamp timeOfRecording;
}
