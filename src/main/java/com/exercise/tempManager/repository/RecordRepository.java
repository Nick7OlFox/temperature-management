package com.exercise.tempManager.repository;

import com.exercise.tempManager.dto.Record;
import com.exercise.tempManager.dto.compk.RecordId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, RecordId> {
}
