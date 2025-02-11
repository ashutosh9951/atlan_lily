package com.atlan.service;

import com.atlan.model.BulkIngestFromFileParams;
import com.atlan.model.BulkIngestFromFileResult;

public interface FileBasedEventProcessingService {
    BulkIngestFromFileResult processFile(BulkIngestFromFileParams bulkIngestFromFileParams);
}
