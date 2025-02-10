package com.atlan.service;

import com.atlan.model.DataIssueEvent;
import com.atlan.model.MetadataAsset;
import com.atlan.store.MetadataStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessorService.class);

    private final MetadataStore metadataStore;
    private final EnrichmentService enrichmentService;
    private final GovernanceService governanceService;
    private final ObjectMapper objectMapper;


    @Autowired
    public EventProcessorService(MetadataStore metadataStore, EnrichmentService enrichmentService, GovernanceService governanceService, ObjectMapper objectMapper) {
        this.metadataStore = metadataStore;
        this.enrichmentService = enrichmentService;
        this.governanceService = governanceService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "metadata-ingestion-topic", groupId = "metadata-processor-group")
    public void processMetadataEvent(String message) {
        logger.info("Received metadata event: {}", message);
        try {
            MetadataAsset metadataAsset = objectMapper.readValue(message, MetadataAsset.class);
            metadataStore.save(metadataAsset);

            enrichmentService.enrichMetadata(metadataAsset);
            governanceService.applyGovernancePolicies(metadataAsset);

        } catch (IOException e) {
            logger.error("Error processing metadata event: {}", message, e);

        }
    }


     @KafkaListener(topics = "data-issue-topic", groupId = "issue-processor-group")
    public void processIssueEvent(String message) {
        logger.info("Received data issue event: {}", message);
        try {
            DataIssueEvent issueEvent = objectMapper.readValue(message, DataIssueEvent.class);
            metadataStore.saveIssueEvent(issueEvent);

        } catch (IOException e) {
            logger.error("Error processing issue event: {}", message, e);
        }
    }

}