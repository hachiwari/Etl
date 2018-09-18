package com.tkurek.wat.Etl.model.stage;

public class StageToWarehouseIdMap {

    private Long id;
    private Long idStage;
    private String stageTableName;
    private Long idWarehouse;
    private String warehouseTableName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getIdWarehouse() {
        return idWarehouse;
    }

    public void setIdWarehouse(Long idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public String getWarehouseTableName() {
        return warehouseTableName;
    }

    public void setWarehouseTableName(String warehouseTableName) {
        this.warehouseTableName = warehouseTableName;
    }
}
