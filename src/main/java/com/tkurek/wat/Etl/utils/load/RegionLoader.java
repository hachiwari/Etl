package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Producer;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Region;
import com.tkurek.wat.Etl.model.warehouse.W_Producer;
import com.tkurek.wat.Etl.model.warehouse.W_Region;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class RegionLoader {

    private static final Logger LOG = LoggerFactory.getLogger(RegionLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_Region.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_Region.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Region: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_Region> allStages = this.stageMapper.selectAllTmpRegion();
        Timestamp lastImport = this.logService.getLastImportToStage(W_Region.class.getSimpleName());

        Collection<Tmp_W_Region> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_Region object : allNewStages) {
            W_Region lastObject = this.warehouseMapper.selectLastWRegion(object.getIdRegion());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWRegion(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_Region newObject = createNew(object);
                this.warehouseMapper.insertWRegion(newObject);
            }
        }
    }

    private W_Region createNew(Tmp_W_Region object) {
        W_Region newObject = new W_Region();
        newObject.setIdRegion(object.getIdRegion());
        newObject.setName(object.getName());
        newObject.setCountry(object.getCountry());
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
