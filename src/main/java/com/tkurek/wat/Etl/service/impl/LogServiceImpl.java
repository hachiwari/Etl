package com.tkurek.wat.Etl.service.impl;

import com.tkurek.wat.Etl.mapper.MetadataMapper;
import com.tkurek.wat.Etl.model.metadata.LogImporter;
import com.tkurek.wat.Etl.service.LogService;

import java.sql.Timestamp;

public class LogServiceImpl implements LogService {

    private MetadataMapper metadataMapper;

    public void logImport(String tableName, Timestamp importTimestamp, Boolean success) {
        LogImporter logImport = new LogImporter();
        logImport.setTableName(tableName);
        logImport.setImportTime(importTimestamp);
        logImport.setSuccess(success);
        this.metadataMapper.insertLogImporter(logImport);
    }

    @Override
    public Timestamp getLastImportToStage(String className) {
        Timestamp logImport = this.metadataMapper.findLastTimestampForTable(className);
        return (logImport == null) ? new Timestamp(0) : logImport;

    }

    public void setMetadataMapper(MetadataMapper metadataMapper) {
        this.metadataMapper = metadataMapper;
    }
}
