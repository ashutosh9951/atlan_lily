package com.atlan.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetadataAssetSearchRepository extends ElasticsearchRepository<MetadataAssetSearchDocument, String> {
    List<MetadataAssetSearchDocument> findByDisplayNameContainingIgnoreCase(String displayName);
}