package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceTwoMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceOne.SourceOne_Brand;
import com.tkurek.wat.Etl.model.sourceTwo.SourceTwo_Shop;
import com.tkurek.wat.Etl.model.sourceTwo.SourceTwo_TypeWorker;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Brand;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Shop;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_TypeWorker;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import com.tkurek.wat.Etl.service.impl.ExtractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceTwoTypeWorkerExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractServiceImpl.class);

    private LogService logService;
    private UtilService utilService;
    private SourceTwoMapper sourceTwoMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceTwo_TypeWorker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceTwo_TypeWorker.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting TypeWorker: ", e);
        }
    }

    private void run() {
        Collection<SourceTwo_TypeWorker> allSources = this.sourceTwoMapper.selectAllTypeWorker();
        Collection<Stage_TypeWorker> allStages = this.stageMapper.selectAllTypeWorker();

        for (SourceTwo_TypeWorker object : allSources) {
            Collection<Stage_TypeWorker> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdTypeWorker().equals(object.getIdTypeWorker())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_TypeWorker newObject = createNew(object);
                this.stageMapper.insertTypeWorker(newObject);
                this.utilService.setSourceToStageId(object.getIdTypeWorker(), newObject.getIdTypeWorker(), SourceTwo_TypeWorker.class, Stage_TypeWorker.class);
            } else {
                for (Stage_TypeWorker stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_TypeWorker newObject = createNew(object);
                            this.stageMapper.updateTypeWorker(stageObject);
                            this.stageMapper.insertTypeWorker(newObject);
                            this.utilService.setSourceToStageId(object.getIdTypeWorker(), newObject.getIdTypeWorker(), SourceTwo_TypeWorker.class, Stage_TypeWorker.class);
                        }
                    }
                }
            }
        }

        for (Stage_TypeWorker oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateTypeWorker(oldObject);
            }
        }
    }

    private Stage_TypeWorker createNew(SourceTwo_TypeWorker object) {
        Stage_TypeWorker newObject = new Stage_TypeWorker();
        newObject.setIdTypeWorker(object.getIdTypeWorker());
        newObject.setName(object.getName());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceTwo_TypeWorker sourceObject, Stage_TypeWorker stageObject) {
        if (!sourceObject.getIdTypeWorker().equals(stageObject.getIdTypeWorker())) {
            return false;
        }
        if (!sourceObject.getName().equals(stageObject.getName())) {
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
