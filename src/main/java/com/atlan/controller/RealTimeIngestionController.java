package com.atlan.controller;

import com.atlan.model.MetadataAsset;
import com.atlan.service.EventBasedIngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingestion/realtime")
public class RealTimeIngestionController {

    private final EventBasedIngestionService eventBasedIngestionService;

    @Autowired
    public RealTimeIngestionController(EventBasedIngestionService eventBasedIngestionService) {
        this.eventBasedIngestionService = eventBasedIngestionService;
    }

    @PostMapping
    public ResponseEntity<String> ingestRealTimeMetadata(@RequestBody MetadataAsset metadataAsset) {
        try {
            eventBasedIngestionService.enqueueRealTimeMetadata(metadataAsset);
            return ResponseEntity.ok("Real-time metadata ingestion request submitted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting real-time ingestion request: " + e.getMessage());
        }
    }
}