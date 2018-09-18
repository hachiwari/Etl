package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Producer;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_TypeWorker;
import com.tkurek.wat.Etl.model.warehouse.W_Producer;
import com.tkurek.wat.Etl.model.warehouse.W_TypeWorker;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class TypeWorkerLoader {

    private static final Logger LOG = LoggerFactory.getLogger(TypeWorkerLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_TypeWorker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_TypeWorker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load TypeWorker: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_TypeWorker> allStages = this.stageMapper.selectAllTmpTypeWorker();
        Timestamp lastImport = this.logService.getLastImportToStage(W_TypeWorker.class.getSimpleName());

        Collection<Tmp_W_TypeWorker> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_TypeWorker object : allNewStages) {
            W_TypeWorker lastObject = this.warehouseMapper.selectLastWTypeWorker(object.getIdTypeWorker());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWTypeWorker(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_TypeWorker newObject = createNew(object);
                this.warehouseMapper.insertWTypeWorker(newObject);
            }
        }
    }

    private W_TypeWorker createNew(Tmp_W_TypeWorker object) {
        W_TypeWorker newObject = new W_TypeWorker();
        newObject.setIdTypeWorker(object.getIdTypeWorker());
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
