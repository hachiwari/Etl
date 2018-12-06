package com.tkurek.wat.Etl.service.impl;

import com.tkurek.wat.Etl.mapper.MetadataMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.service.CleanService;

import java.util.*;

public class CleanServiceImpl implements CleanService  {

    private MetadataMapper metadataMapper;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    private static final String PHASE_METADATA = "METADATA";
    private static final String PHASE_STAGE = "STAGE";
    private static final String PHASE_WAREHOUSE = "WAREHOUSE";

    private final static Map<String, List<String>> allTables = new HashMap<String, List<String>>() {{
        put(PHASE_METADATA, Arrays.asList("logImporter", "badDelivery", "badSale"));
        put(PHASE_STAGE, Arrays.asList(
            "brand", "country", "delivery", "locality", "producer", "product", "provider", "region", "sale", "shop", "typePrice", "typeWorker", "worker",
            "tmpWBrand", "tmpWCountry", "tmpFDelivery", "tmpWLocality", "tmpWProducer", "tmpWProduct", "tmpWProvider", "tmpWRegion", "tmpFSale", "tmpWShop", "tmpWTypePrice", "tmpWTypeWorker", "tmpWWorker",
            "sourceToStageIdMap", "stageToWarehouseIdMap"
        ));
        put(PHASE_WAREHOUSE, Arrays.asList("WBrand", "WCountry", "FDelivery", "WLocality", "WProducer", "WProduct", "WProvider", "WRegion", "FSale", "WShop", "WTypePrice", "WTypeWorker", "WWorker"));
    }};

    @Override
    public void cleanAllPhases() {
        cleanPhase(PHASE_METADATA);
        cleanPhase(PHASE_STAGE);
        cleanPhase(PHASE_WAREHOUSE);
    }

    @Override
    public boolean cleanPhase(String phaseName) {
        switch (phaseName.toUpperCase()) {
            case PHASE_METADATA:
                for (String tableName : allTables.get(PHASE_METADATA)) {
                    metadataMapper.cleanTable(tableName);
                }
                break;
            case PHASE_STAGE:
                for (String tableName : allTables.get(PHASE_STAGE)) {
                    stageMapper.cleanTable(tableName);
                }
                break;
             case PHASE_WAREHOUSE:
                 for (String tableName : allTables.get(PHASE_WAREHOUSE)) {
                     warehouseMapper.cleanTable(tableName);
                 }
                break;
            default:
                return false;
        }
        return true;
    }

    public void setMetadataMapper(MetadataMapper metadataMapper) {
        this.metadataMapper = metadataMapper;
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }

    public void setWarehouseMapper(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }
}
