package com.atlan.store;

import com.atlan.model.DataIssueEvent;
import com.atlan.model.MetadataAsset;

public interface MetadataStore {
    void save(MetadataAsset metadataAsset);
    MetadataAsset get(String guid);
    void saveIssueEvent(DataIssueEvent issueEvent);
}