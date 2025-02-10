package com.atlan.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Table extends MetadataAsset {
    private String type = "Table";
    private String schemaName;
    private String databaseQualifiedName;

    public Table(String qualifiedName, String displayName, String schemaName, String databaseQualifiedName) {
        super(qualifiedName, displayName);
        this.schemaName = schemaName;
        this.databaseQualifiedName = databaseQualifiedName;
    }
}