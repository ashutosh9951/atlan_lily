package com.atlan.repository;

import com.atlan.model.DataIssueEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataIssueEventRepository extends JpaRepository<DataIssueEventEntity, String> {
}
