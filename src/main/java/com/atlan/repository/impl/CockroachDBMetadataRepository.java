package com.atlan.repository.impl;

import com.atlan.event.MetadataAssetSavedEvent;
import com.atlan.model.*;
import com.atlan.repository.MetadataAssetRepository;
import com.atlan.repository.DataIssueEventRepository;
import com.atlan.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Repository
public class CockroachDBMetadataRepository implements MetadataRepository {

    private final MetadataAssetRepository metadataAssetRepository;
    private final DataIssueEventRepository dataIssueEventRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CockroachDBMetadataRepository(MetadataAssetRepository metadataAssetRepository, DataIssueEventRepository dataIssueEventRepository, ApplicationEventPublisher eventPublisher) {
        this.metadataAssetRepository = metadataAssetRepository;
        this.dataIssueEventRepository = dataIssueEventRepository;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public void save(MetadataAsset metadataAsset) {
        MetadataAssetEntity entity = convertToEntity(metadataAsset);
        metadataAssetRepository.save(entity);
        System.out.println("Metadata Asset Saved to CockroachDB: " + metadataAsset.getQualifiedName() + " - Type: " + metadataAsset.getType());

        eventPublisher.publishEvent(new MetadataAssetSavedEvent(this, entity));
    }

    @Override
    public MetadataAsset get(String guid) {
        MetadataAssetEntity entity = metadataAssetRepository.findById(guid).orElse(null);
        if (entity != null) {
            return convertToModel(entity);
        }
        return null;
    }

    @Override
    public void saveIssueEvent(DataIssueEvent issueEvent) {
        DataIssueEventEntity entity = convertToIssueEventEntity(issueEvent);
        dataIssueEventRepository.save(entity);
        System.out.println("Data Issue Event Saved to CockroachDB: " + issueEvent.getIssueType() + " for asset: " + issueEvent.getAssetQualifiedName());
    }

    private MetadataAssetEntity convertToEntity(MetadataAsset model) {
        MetadataAssetEntity entity = null;
        if (model instanceof Database) {
            DatabaseEntity dbEntity = new DatabaseEntity(model.getQualifiedName(), model.getDisplayName(), ((Database) model).getVendor(), ((Database) model).getVersion());
            entity = dbEntity;
        } else if (model instanceof Table) {
            TableEntity tableEntity = new TableEntity(model.getQualifiedName(), model.getDisplayName(), ((Table) model).getSchemaName(), ((Table) model).getDatabaseQualifiedName());
            entity = tableEntity;
        } else if (model instanceof Column) {
            ColumnEntity columnEntity = new ColumnEntity(model.getQualifiedName(), model.getDisplayName(), ((Column) model).getDataType(), ((Column) model).getTableQualifiedName());
            entity = columnEntity;
        }
        if (entity != null) {
            entity.setGuid(model.getGuid());
            entity.setDescription(model.getDescription());
            entity.setCreateTime(model.getCreateTime());
            entity.setUpdateTime(model.getUpdateTime());
            entity.setAttributes(model.getAttributes());
            entity.setType(model.getType());
        }
        return entity;
    }


    private MetadataAsset convertToModel(MetadataAssetEntity entity) {
        MetadataAsset model = null;
        if (entity.getType().equals("Database")) {
            Database dbModel = new Database(entity.getQualifiedName(), entity.getDisplayName(), entity.getAttributes().get("vendor"), entity.getAttributes().get("version"));
            model = dbModel;
        } else if (entity.getType().equals("Table")) {
            Table tableModel = new Table(entity.getQualifiedName(), entity.getDisplayName(), entity.getAttributes().get("schemaName"), entity.getAttributes().get("databaseQualifiedName"));
            model = tableModel;
        } else if (entity.getType().equals("Column")) {
            Column columnModel = new Column(entity.getQualifiedName(), entity.getDisplayName(), entity.getAttributes().get("dataType"), entity.getAttributes().get("tableQualifiedName"));
            model = columnModel;
        }
        if (model != null) {
            model.setGuid(entity.getGuid());
            model.setDescription(entity.getDescription());
            model.setCreateTime(entity.getCreateTime());
            model.setUpdateTime(entity.getUpdateTime());
            model.setAttributes(entity.getAttributes());
            model.setType(entity.getType());
        }
        return model;
    }


    private DataIssueEventEntity convertToIssueEventEntity(DataIssueEvent model) {
        DataIssueEventEntity entity = new DataIssueEventEntity();
        entity.setIssueType(model.getIssueType());
        entity.setAssetQualifiedName(model.getAssetQualifiedName());
        entity.setDescription(model.getDescription());
        entity.setRaisedTime(model.getRaisedTime());
        entity.setDetails(model.getDetails());
        return entity;
    }
}