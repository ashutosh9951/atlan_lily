package com.atlan.service;

import com.atlan.model.DataIssueEvent;
import com.atlan.model.MetadataAsset;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaEventBasedIngestionService implements EventBasedIngestionService {

    private static final String METADATA_TOPIC = "metadata-ingestion-topic";
    private static final String ISSUE_TOPIC = "data-issue-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaEventBasedIngestionService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void enqueueBulkMetadata(List<MetadataAsset> metadataAssets) {
        try {
            for (MetadataAsset asset : metadataAssets) {
                String message = objectMapper.writeValueAsString(asset);
                kafkaTemplate.send(METADATA_TOPIC, message);
            }
        } catch (Exception e) {

            throw new RuntimeException("Error enqueuing bulk metadata to Kafka", e);
        }
    }

    @Override
    public void enqueueRealTimeMetadata(MetadataAsset metadataAsset) {
        try {
            String message = objectMapper.writeValueAsString(metadataAsset);
            kafkaTemplate.send(METADATA_TOPIC, message);
        } catch (Exception e) {
            throw new RuntimeException("Error enqueuing real-time metadata to Kafka", e);
        }
    }

    @Override
    public void enqueueDataIssueEvent(DataIssueEvent dataIssueEvent) {
        try {
            String message = objectMapper.writeValueAsString(dataIssueEvent);
            kafkaTemplate.send(ISSUE_TOPIC, message);
        } catch (Exception e) {
            throw new RuntimeException("Error enqueuing data issue event to Kafka", e);
        }
    }
}