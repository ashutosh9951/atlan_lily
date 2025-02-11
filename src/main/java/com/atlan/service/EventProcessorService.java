package com.atlan.service;

public interface EventProcessorService {
    void processMetadataEvent(String message);
    void processIssueEvent(String message);
}
