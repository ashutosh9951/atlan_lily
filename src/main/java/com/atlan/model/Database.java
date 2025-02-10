package com.atlan.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Database extends MetadataAsset {
    private String type = "Database";
    private String vendor;
    private String version;

    public Database(String qualifiedName, String displayName, String vendor, String version) {
        super(qualifiedName, displayName);
        this.vendor = vendor;
        this.version = version;
    }
}