package com.atlan.model;

public class BulkIngestFromFileResult {
    SyncStatus syncStatus;
    String sucessfullySyncedRecordsFileLocation ;
    String failedRecordsLocation;
    enum SyncStatus{
        SUCCESSFULL, FALED, PARTIALY_SUCCESSFULL
    }
}
