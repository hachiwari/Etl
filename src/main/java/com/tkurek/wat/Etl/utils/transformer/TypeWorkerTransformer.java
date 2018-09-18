package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_TypeWorker;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_TypeWorker;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class TypeWorkerTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(TypeWorkerTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_TypeWorker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_TypeWorker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform TypeWorker: ", e);
        }
    }

    private void run() {
        Collection<Stage_TypeWorker> allObjects = this.stageMapper.selectAllTypeWorker();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_TypeWorker.class.getSimpleName());

        Collection<Stage_TypeWorker> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_TypeWorker object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_TypeWorker newObject = createNew(object);
                Tmp_W_TypeWorker lastObject = this.stageMapper.selectLastTmpTypeWorker(object.getIdTypeWorker());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpTypeWorker(lastObject);
                }

                this.stageMapper.insertTmpTypeWorker(newObject);
                this.utilService.setStageToWarehouseId(object.getIdTypeWorker(), newObject.getIdTypeWorker(), Stage_TypeWorker.class, Tmp_W_TypeWorker.class);
            } else {
                Tmp_W_TypeWorker lastObject = this.stageMapper.selectLastTmpTypeWorker(object.getIdTypeWorker());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpTypeWorker(lastObject);
                }
            }
        }
    }

    private Tmp_W_TypeWorker createNew(Stage_TypeWorker object) {
        Tmp_W_TypeWorker newObject = new Tmp_W_TypeWorker();
        newObject.setIdTypeWorker(object.getIdTypeWorker());
        newObject.setName(object.getName());
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
