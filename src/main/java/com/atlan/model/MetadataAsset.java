package com.atlan.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Database.class, name = "Database"),
        @JsonSubTypes.Type(value = Table.class, name = "Table"),
        @JsonSubTypes.Type(value = Column.class, name = "Column")
})
public abstract class MetadataAsset {
    private String guid = UUID.randomUUID().toString();
    private String qualifiedName;
    private String displayName;
    private String description;
    private String type;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();
    private Map<String, String> attributes = new java.util.HashMap<>();

    public MetadataAsset(String qualifiedName, String displayName) {
        this.qualifiedName = qualifiedName;
        this.displayName = displayName;
    }
}