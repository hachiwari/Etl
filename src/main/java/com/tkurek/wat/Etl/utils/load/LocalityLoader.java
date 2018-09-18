package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Locality;
import com.tkurek.wat.Etl.model.warehouse.W_Locality;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class LocalityLoader {

    private static final Logger LOG = LoggerFactory.getLogger(LocalityLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_Locality.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_Locality.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Locality: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_Locality> allStages = this.stageMapper.selectAllTmpLocality();
        Timestamp lastImport = this.logService.getLastImportToStage(W_Locality.class.getSimpleName());

        Collection<Tmp_W_Locality> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_Locality object : allNewStages) {
            W_Locality lastObject = this.warehouseMapper.selectLastWLocality(object.getIdLocality());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWLocality(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_Locality newObject = createNew(object);
                this.warehouseMapper.insertWLocality(newObject);
            }
        }
    }

    private W_Locality createNew(Tmp_W_Locality object) {
        W_Locality newObject = new W_Locality();
        newObject.setIdLocality(object.getIdLocality());
        newObject.setIdRegion(object.getIdRegion());
        newObject.setName(object.getName());
        newObject.setTimestampFrom(object.getTimestampFrom());
        newObject.setTimestampTo(object.getTimestampTo());
        return newObject;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }

    public void setWarehouseMapper(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }
}
