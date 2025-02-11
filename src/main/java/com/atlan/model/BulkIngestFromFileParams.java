package com.atlan.model;

import lombok.Data;

@Data
public class BulkIngestFromFileParams {
    String fileLoc;
    String dbName;
    String tableName;
}
