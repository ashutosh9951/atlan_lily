package com.atlan.search;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "metadata-assets-index")
@Data
@NoArgsConstructor
public class MetadataAssetSearchDocument {

    @Id
    private String guid;

    @Field(type = FieldType.Text)
    private String qualifiedName;

    @Field(type = FieldType.Text)
    private String displayName;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String assetType;

    public MetadataAssetSearchDocument(String guid, String qualifiedName, String displayName, String description, String assetType) {
        this.guid = guid;
        this.qualifiedName = qualifiedName;
        this.displayName = displayName;
        this.description = description;
        this.assetType = assetType;
    }
}