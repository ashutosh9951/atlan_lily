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
@DiscriminatorValue("Database")
public class DatabaseEntity extends MetadataAssetEntity {

    private String vendor;
    private String version;

    public DatabaseEntity(String qualifiedName, String displayName, String vendor, String version) {
        super("Database", qualifiedName, displayName);
        this.vendor = vendor;
        this.version = version;
    }
}