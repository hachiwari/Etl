package com.tkurek.wat.Etl.service;

import java.sql.Timestamp;

public interface LogService {

    void logImport(String tableName, Timestamp importTimestamp, Boolean success);
    Timestamp getLastImportToStage(String className);
}
