package com.atlan.repository;

import com.atlan.model.DataIssueEvent;
import com.atlan.model.MetadataAsset;

public interface MetadataRepository {
    void save(MetadataAsset metadataAsset);
    MetadataAsset get(String guid);
    void saveIssueEvent(DataIssueEvent issueEvent);
}