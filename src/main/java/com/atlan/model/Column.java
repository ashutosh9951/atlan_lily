package com.atlan.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Column extends MetadataAsset {
    private String type = "Column";
    private String dataType;
    private String tableQualifiedName;
    private boolean isPrimaryKey;
    private boolean isNullable;

    public Column(String qualifiedName, String displayName, String dataType, String tableQualifiedName) {
        super(qualifiedName, displayName);
        this.dataType = dataType;
        this.tableQualifiedName = tableQualifiedName;
    }
}