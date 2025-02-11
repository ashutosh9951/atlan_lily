package com.atlan.model;


import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "asset_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "metadata_assets")
public abstract class MetadataAssetEntity {

    @Id
    private String guid = UUID.randomUUID().toString();

    @jakarta.persistence.Column(name = "asset_type", insertable = false, updatable = false, nullable = false, length = 50)
    private String type; // Discriminator column

    @jakarta.persistence.Column( unique = true, nullable = false)
    private String qualifiedName;

    private String displayName;
    private String description;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, String> attributes = new java.util.HashMap<>();

    public MetadataAssetEntity(String type, String qualifiedName, String displayName) {
        this.type = type;
        this.qualifiedName = qualifiedName;
        this.displayName = displayName;
    }
}