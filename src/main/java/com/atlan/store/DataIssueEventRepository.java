package com.atlan.store;

import com.atlan.model.DataIssueEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataIssueEventRepository extends JpaRepository<DataIssueEventEntity, String> {
}
