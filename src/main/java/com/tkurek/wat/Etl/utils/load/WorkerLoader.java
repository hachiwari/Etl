package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Worker;
import com.tkurek.wat.Etl.model.warehouse.W_Worker;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class WorkerLoader {

    private static final Logger LOG = LoggerFactory.getLogger(WorkerLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_Worker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_Worker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Worker: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_Worker> allStages = this.stageMapper.selectAllTmpWorker();
        Timestamp lastImport = this.logService.getLastImportToStage(W_Worker.class.getSimpleName());

        Collection<Tmp_W_Worker> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_Worker object : allNewStages) {
            W_Worker lastObject = this.warehouseMapper.selectLastWWorker(object.getIdWorker());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWWorker(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_Worker newObject = createNew(object);
                this.warehouseMapper.insertWWorker(newObject);
            }
        }
    }

    private W_Worker createNew(Tmp_W_Worker object) {
        W_Worker newObject = new W_Worker();
        newObject.setIdWorker(object.getIdWorker());
        newObject.setFirstName(object.getFirstName());
        newObject.setLastName(object.getLastName());
        newObject.setIdTypeWorker(object.getIdTypeWorker());
        newObject.setPesel(object.getPesel());
        newObject.setPhone(object.getPhone());
        newObject.setAddress(object.getAddress());
        newObject.setCity(object.getCity());
        newObject.setZipCode(object.getZipCode());
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
