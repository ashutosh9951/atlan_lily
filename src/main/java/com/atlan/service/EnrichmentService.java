package com.atlan.service;

import com.atlan.model.MetadataAsset;
import org.springframework.stereotype.Service;

@Service
public class EnrichmentService {

    public void enrichMetadata(MetadataAsset metadataAsset) {
        System.out.println("Performing Enrichment for: " + metadataAsset.getQualifiedName() + " - Type: " + metadataAsset.getType());
    }
}