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
@DiscriminatorValue("Column")
public class ColumnEntity extends MetadataAssetEntity {

    private String dataType;
    private String tableQualifiedName;
    private boolean isPrimaryKey;
    private boolean isNullable;

    public ColumnEntity(String qualifiedName, String displayName, String dataType, String tableQualifiedName) {
        super("Column", qualifiedName, displayName);
        this.dataType = dataType;
        this.tableQualifiedName = tableQualifiedName;
    }
}