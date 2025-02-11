package com.atlan.controller;

import com.atlan.model.DataIssueEvent;
import com.atlan.service.EventBasedIngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingestion/montecarlo")
public class MonteCarloWebhookController {

    private final EventBasedIngestionService eventBasedIngestionService;

    @Autowired
    public MonteCarloWebhookController(EventBasedIngestionService eventBasedIngestionService) {
        this.eventBasedIngestionService = eventBasedIngestionService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMonteCarloEvent(@RequestBody DataIssueEvent dataIssueEvent) {
        try {
            eventBasedIngestionService.enqueueDataIssueEvent(dataIssueEvent);
            return ResponseEntity.ok("Monte Carlo event received and processed.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Monte Carlo event: " + e.getMessage());
        }
    }
}