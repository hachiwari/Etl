package com.tkurek.wat.Etl.model.metadata;

import java.sql.Timestamp;

public class LogImporter {

    private Long id;
    private String tableName;
    private Timestamp importTime;
    private Boolean success;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Timestamp getImportTime() {
        return importTime;
    }

    public void setImportTime(Timestamp importTime) {
        this.importTime = importTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
