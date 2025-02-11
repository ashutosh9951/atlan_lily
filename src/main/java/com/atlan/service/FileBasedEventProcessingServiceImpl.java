package com.atlan.service;

import com.atlan.model.BulkIngestFromFileParams;
import com.atlan.model.BulkIngestFromFileResult;
import org.springframework.stereotype.Service;

@Service
public class FileBasedEventProcessingServiceImpl implements FileBasedEventProcessingService {
    @Override
    public BulkIngestFromFileResult processFile(BulkIngestFromFileParams bulkIngestFromFileParams) {
        //TODO: Implement read from file and create IMPORT statement for bulk load
        return null;
    }
}
