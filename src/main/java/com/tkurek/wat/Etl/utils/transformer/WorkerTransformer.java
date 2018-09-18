package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Worker;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Worker;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class WorkerTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(WorkerTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_Worker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_Worker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Worker: ", e);
        }
    }

    private void run() {
        Collection<Stage_Worker> allObjects = this.stageMapper.selectAllWorker();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_Worker.class.getSimpleName());

        Collection<Stage_Worker> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_Worker object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_Worker newObject = createNew(object);
                Tmp_W_Worker lastObject = this.stageMapper.selectLastTmpWorker(object.getIdWorker());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpWorker(lastObject);
                }

                this.stageMapper.insertTmpWorker(newObject);
                this.utilService.setStageToWarehouseId(object.getIdWorker(), newObject.getIdWorker(), Stage_Worker.class, Tmp_W_Worker.class);
            } else {
                Tmp_W_Worker lastObject = this.stageMapper.selectLastTmpWorker(object.getIdWorker());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpWorker(lastObject);
                }
            }
        }
    }

    private Tmp_W_Worker createNew(Stage_Worker object) {
        Tmp_W_Worker newObject = new Tmp_W_Worker();
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

    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }
}
