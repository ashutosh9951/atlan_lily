package com.atlan.controller;

import com.atlan.model.MetadataAsset;
import com.atlan.service.IngestionService;
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

    private final IngestionService ingestionService;

    @Autowired
    public RealTimeIngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public ResponseEntity<String> ingestRealTimeMetadata(@RequestBody MetadataAsset metadataAsset) {
        try {
            ingestionService.enqueueRealTimeMetadata(metadataAsset);
            return ResponseEntity.ok("Real-time metadata ingestion request submitted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting real-time ingestion request: " + e.getMessage());
        }
    }
}