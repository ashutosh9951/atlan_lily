package com.atlan.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Table")
public class TableEntity extends MetadataAssetEntity {

    private String schemaName;
    private String databaseQualifiedName;

    public TableEntity(String qualifiedName, String displayName, String schemaName, String databaseQualifiedName) {
        super("Table", qualifiedName, displayName);
        this.schemaName = schemaName;
        this.databaseQualifiedName = databaseQualifiedName;
    }
}