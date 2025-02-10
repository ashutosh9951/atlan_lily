package com.atlan.service;

import com.atlan.event.MetadataAssetSavedEvent;
import com.atlan.model.MetadataAssetEntity;
import com.atlan.search.MetadataAssetSearchDocument;
import com.atlan.search.MetadataAssetSearchRepository;
import com.atlan.store.MetadataAssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetadataSynchronizationService {

    private static final Logger logger = LoggerFactory.getLogger(MetadataSynchronizationService.class);
    private final MetadataAssetSearchRepository searchRepository;

    @Autowired
    public MetadataSynchronizationService(MetadataAssetSearchRepository searchRepository, MetadataAssetRepository metadataAssetRepository) {
        this.searchRepository = searchRepository;
    }

    @Async
    @EventListener
    public void handleMetadataAssetSavedEvent(MetadataAssetSavedEvent event) {
        MetadataAssetEntity entity = event.getMetadataAssetEntity();
        indexMetadataAssetForSearch(entity);
    }


    public void indexMetadataAssetForSearch(MetadataAssetEntity entity) {
        try {
            MetadataAssetSearchDocument searchDocument = convertToSearchDocument(entity);
            searchRepository.save(searchDocument); // Index to Elasticsearch
            logger.info("Metadata Asset indexed to Elasticsearch: {}", entity.getQualifiedName());
        } catch (Exception e) {
            logger.error("Error indexing metadata asset to Elasticsearch: {}", entity.getQualifiedName(), e);
        }
    }

    private MetadataAssetSearchDocument convertToSearchDocument(MetadataAssetEntity entity) {
        MetadataAssetSearchDocument doc = new MetadataAssetSearchDocument();
        doc.setGuid(entity.getGuid());
        doc.setQualifiedName(entity.getQualifiedName());
        doc.setDisplayName(entity.getDisplayName());
        doc.setDescription(entity.getDescription());
        doc.setAssetType(entity.getType());

        return doc;
    }
    public List<MetadataAssetSearchDocument> searchAssetsByText(String searchText) {

        return searchRepository.findByDisplayNameContainingIgnoreCase(searchText); // Simple example

    }
}