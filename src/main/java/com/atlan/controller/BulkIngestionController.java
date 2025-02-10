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

import java.util.List;

@RestController
@RequestMapping("/api/ingestion/bulk")
public class BulkIngestionController {

    private final IngestionService ingestionService;

    @Autowired
    public BulkIngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public ResponseEntity<String> ingestBulkMetadata(@RequestBody List<MetadataAsset> metadataAssets) {
        try {
            ingestionService.enqueueBulkMetadata(metadataAssets);
            return ResponseEntity.ok("Bulk metadata ingestion request submitted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting bulk ingestion request: " + e.getMessage());
        }
    }
}