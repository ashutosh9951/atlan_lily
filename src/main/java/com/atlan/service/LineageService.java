package com.atlan.service;
//
//import com.atlan.event.MetadataAssetSavedEvent;
//import com.atlan.model.MetadataAssetEntity;
//import com.atlan.store.MetadataAssetRepository;
//import org.neo4j.driver.Driver;
//import org.neo4j.driver.Session;
//import org.neo4j.driver.Transaction;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
////@Service

//TODO: Fix this
//public class LineageService {
//
//    private static final Logger logger = LoggerFactory.getLogger(LineageService.class);
//    private final Driver neo4jDriver;
//
//    @Autowired
//    public LineageService(Driver neo4jDriver, MetadataAssetRepository metadataAssetRepository) {
//        this.neo4jDriver = neo4jDriver;
//    }
//
//    @Async
//    @EventListener
//    public void handleMetadataAssetSavedEvent(MetadataAssetSavedEvent event) {
//        MetadataAssetEntity entity = event.getMetadataAssetEntity();
//        createLineageNodesAndRelationships(entity);
//    }
//
//    public void createLineageNodesAndRelationships(MetadataAssetEntity entity) {
//        try (Session session = neo4jDriver.session()) {
//
//            session.writeTransaction(transaction -> {
//                createAssetNode(transaction, entity);
//
//                if (entity.getType().equals("Table")) {
//                    String databaseQualifiedName = entity.getAttributes().get("databaseQualifiedName");
//                    if (databaseQualifiedName != null) {
//                        createLineageRelationship(transaction, entity.getQualifiedName(), databaseQualifiedName, "PART_OF");
//                    }
//                }
//                return null;
//            });
//            logger.info("Lineage updated in Neo4j for asset: {}", entity.getQualifiedName());
//
//        } catch (Exception e) {
//            logger.error("Error updating lineage in Neo4j for asset: {}", entity.getQualifiedName(), e);
//        }
//    }
//
//    private void createAssetNode(Transaction tx, MetadataAssetEntity entity) {
//        tx.run("MERGE (asset:Asset {qualifiedName: $qualifiedName}) " +
//                       "SET asset.guid = $guid, asset.displayName = $displayName, asset.assetType = $assetType",
//                org.neo4j.driver.Values.parameters(
//                        "qualifiedName", entity.getQualifiedName(),
//                        "guid", entity.getGuid(),
//                        "displayName", entity.getDisplayName(),
//                        "assetType", entity.getType()
//                ));
//    }
//
//    private void createLineageRelationship(Transaction tx, String fromAssetQualifiedName, String toAssetQualifiedName, String relationshipType) {
//        tx.run("MATCH (from:Asset {qualifiedName: $fromQN}), (to:Asset {qualifiedName: $toQN}) " +
//                       "MERGE (from)-[:" + relationshipType + "]->(to)",
//                org.neo4j.driver.Values.parameters("fromQN", fromAssetQualifiedName, "toQN", toAssetQualifiedName));
//    }
//}