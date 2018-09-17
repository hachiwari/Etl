package com.tkurek.wat.Etl.model.stage;

public class SourceToStageIdMap {

    private Long id;
    private Long idSource;
    private String sourceTableName;
    private Long idStage;
    private String stageTableName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSource() {
        return idSource;
    }

    public void setIdSource(Long idSource) {
        this.idSource = idSource;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public Long getIdStage() {
        return idStage;
    }

    public void setIdStage(Long idStage) {
        this.idStage = idStage;
    }

    public String getStageTableName() {
        return stageTableName;
    }

    public void setStageTableName(String stageTableName) {
        this.stageTableName = stageTableName;
    }
}
