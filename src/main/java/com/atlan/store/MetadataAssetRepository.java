package com.atlan.store;

import com.atlan.model.MetadataAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataAssetRepository extends JpaRepository<MetadataAssetEntity, String> {

}