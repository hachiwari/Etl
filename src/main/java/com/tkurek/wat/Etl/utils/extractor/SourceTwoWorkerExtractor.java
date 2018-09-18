package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceTwoMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceTwo.SourceTwo_Worker;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Worker;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceTwoWorkerExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SourceTwoWorkerExtractor.class);

    private LogService logService;
    private UtilService utilService;
    private SourceTwoMapper sourceTwoMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceTwo_Worker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceTwo_Worker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Worker: ", e);
        }
    }

    private void run() {
        Collection<SourceTwo_Worker> allSources = this.sourceTwoMapper.selectAllWorker();
        Collection<Stage_Worker> allStages = this.stageMapper.selectAllWorker();

        for (SourceTwo_Worker object : allSources) {
            Collection<Stage_Worker> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdWorker().equals(object.getIdWorker())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Worker newObject = createNew(object);
                this.stageMapper.insertWorker(newObject);
                this.utilService.setSourceToStageId(object.getIdWorker(), newObject.getIdWorker(), SourceTwo_Worker.class, Stage_Worker.class);
            } else {
                for (Stage_Worker stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Worker newObject = createNew(object);
                            this.stageMapper.updateWorker(stageObject);
                            this.stageMapper.insertWorker(newObject);
                            this.utilService.setSourceToStageId(object.getIdWorker(), newObject.getIdWorker(), SourceTwo_Worker.class, Stage_Worker.class);
                        }
                    }
                }
            }
        }

        for (Stage_Worker oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateWorker(oldObject);
            }
        }
    }

    private Stage_Worker createNew(SourceTwo_Worker object) {
        Stage_Worker newObject = new Stage_Worker();
        newObject.setIdWorker(object.getIdWorker());
        newObject.setFirstName(object.getFirstName());
        newObject.setLastName(object.getLastName());
        newObject.setIdTypeWorker(object.getIdTypeWorker());
        newObject.setPesel(object.getPesel());
        newObject.setPhone(object.getPhone());
        newObject.setAddress(object.getAddress());
        newObject.setCity(object.getCity());
        newObject.setZipCode(object.getZipCode());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceTwo_Worker sourceObject, Stage_Worker stageObject) {
        if (!sourceObject.getIdWorker().equals(stageObject.getIdWorker())) {
            return false;
        }
        if (!sourceObject.getFirstName().equals(stageObject.getFirstName())) {
            return false;
        }
        if (!sourceObject.getLastName().equals(stageObject.getLastName())) {
            return false;
        }
        if (!sourceObject.getIdTypeWorker().equals(stageObject.getIdTypeWorker())) {
            return false;
        }
        if (!sourceObject.getPesel().equals(stageObject.getPesel())) {
            return false;
        }
        if (!sourceObject.getPhone().equals(stageObject.getPhone())) {
            return false;
        }
        if (!sourceObject.getAddress().equals(stageObject.getAddress())) {
            return false;
        }
        if (!sourceObject.getCity().equals(stageObject.getCity())) {
            return false;
        }
        if (!sourceObject.getZipCode().equals(stageObject.getZipCode())) {
            return false;
        }

        return true;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    public void setSourceTwoMapper(SourceTwoMapper sourceTwoMapper) {
        this.sourceTwoMapper = sourceTwoMapper;
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }
}
