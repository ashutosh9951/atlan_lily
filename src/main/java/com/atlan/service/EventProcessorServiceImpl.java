package com.atlan.service;

import com.atlan.model.DataIssueEvent;
import com.atlan.model.MetadataAsset;
import com.atlan.repository.MetadataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventProcessorServiceImpl implements EventProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessorServiceImpl.class);

    private final MetadataRepository metadataRepository;
    private final EnrichmentService enrichmentService;
    private final GovernanceService governanceService;
    private final ObjectMapper objectMapper;


    @Autowired
    public EventProcessorServiceImpl(MetadataRepository metadataRepository, EnrichmentService enrichmentService, GovernanceService governanceService, ObjectMapper objectMapper) {
        this.metadataRepository = metadataRepository;
        this.enrichmentService = enrichmentService;
        this.governanceService = governanceService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "metadata-ingestion-topic", groupId = "metadata-processor-group")
    @Override
    public void processMetadataEvent(String message) {
        logger.info("Received metadata event: {}", message);
        try {
            MetadataAsset metadataAsset = objectMapper.readValue(message, MetadataAsset.class);
            metadataRepository.save(metadataAsset);

            enrichmentService.enrichMetadata(metadataAsset);
            governanceService.applyGovernancePolicies(metadataAsset);

        } catch (IOException e) {
            logger.error("Error processing metadata event: {}", message, e);

        }
    }


     @KafkaListener(topics = "data-issue-topic", groupId = "issue-processor-group")
     @Override
     public void processIssueEvent(String message) {
        logger.info("Received data issue event: {}", message);
        try {
            DataIssueEvent issueEvent = objectMapper.readValue(message, DataIssueEvent.class);
            metadataRepository.saveIssueEvent(issueEvent);

        } catch (IOException e) {
            logger.error("Error processing issue event: {}", message, e);
        }
    }

}