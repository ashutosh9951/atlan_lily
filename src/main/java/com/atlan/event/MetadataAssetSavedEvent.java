package com.atlan.event;

import com.atlan.model.MetadataAssetEntity;
import org.springframework.context.ApplicationEvent;

public class MetadataAssetSavedEvent extends ApplicationEvent {

    private final MetadataAssetEntity metadataAssetEntity;

    public MetadataAssetSavedEvent(Object source, MetadataAssetEntity metadataAssetEntity) {
        super(source);
        this.metadataAssetEntity = metadataAssetEntity;
    }

    public MetadataAssetEntity getMetadataAssetEntity() {
        return metadataAssetEntity;
    }
}