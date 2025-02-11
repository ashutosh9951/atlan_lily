package com.atlan.service;

import com.atlan.model.DataIssueEvent;
import com.atlan.model.MetadataAsset;
import java.util.List;

public interface EventBasedIngestionService {
    void enqueueBulkMetadata(List<MetadataAsset> metadataAssets);
    void enqueueRealTimeMetadata(MetadataAsset metadataAsset);
    void enqueueDataIssueEvent(DataIssueEvent dataIssueEvent);
}