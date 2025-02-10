package com.atlan.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class DataIssueEvent {
    private String issueType;
    private String assetQualifiedName;
    private String description;
    private LocalDateTime raisedTime = LocalDateTime.now();
    private Map<String, String> details = new java.util.HashMap<>();
}