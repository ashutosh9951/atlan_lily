package com.atlan.model;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "data_issue_events")
public class DataIssueEventEntity {

    @Id
    private String id = UUID.randomUUID().toString();

    private String issueType;
    private String assetQualifiedName;
    private String description;
    private LocalDateTime raisedTime = LocalDateTime.now();

    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "TEXT") // Or consider "jsonb"
    private Map<String, String> details = new java.util.HashMap<>();
}