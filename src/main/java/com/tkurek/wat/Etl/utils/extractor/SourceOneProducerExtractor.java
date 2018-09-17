package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceOneMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceOne.SourceOne_Producer;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Producer;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import com.tkurek.wat.Etl.service.impl.ExtractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceOneProducerExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractServiceImpl.class);

    private LogService logService;
    private UtilService utilService;
    private SourceOneMapper sourceOneMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceOne_Producer.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceOne_Producer.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Producer: ", e);
        }
    }

    private void run() {
        Collection<SourceOne_Producer> allSources = this.sourceOneMapper.selectAllProducer();
        Collection<Stage_Producer> allStages = this.stageMapper.selectAllProducer();

        for (SourceOne_Producer object : allSources) {
            Collection<Stage_Producer> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdProducer().equals(object.getIdProducer())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Producer newObject = createNew(object);
                this.stageMapper.insertProducer(newObject);
                this.utilService.setSourceToStageId(object.getIdProducer(), newObject.getIdProducer(), SourceOne_Producer.class, Stage_Producer.class);
            } else {
                for (Stage_Producer stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Producer newObject = createNew(object);
                            this.stageMapper.updateProducer(stageObject);
                            this.stageMapper.insertProducer(newObject);
                            this.utilService.setSourceToStageId(object.getIdProducer(), newObject.getIdProducer(), SourceOne_Producer.class, Stage_Producer.class);
                        }
                    }
                }
            }
        }

        for (Stage_Producer oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateProducer(oldObject);
            }
        }
    }

    private Stage_Producer createNew(SourceOne_Producer object) {
        Stage_Producer newObject = new Stage_Producer();
        newObject.setIdProducer(object.getIdProducer());
        newObject.setName(object.getName());
        newObject.setAddress(object.getAddress());
        newObject.setCity(object.getCity());
        newObject.setZipCode(object.getZipCode());
        newObject.setPhone(object.getPhone());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceOne_Producer sourceObject, Stage_Producer stageObject) {
        if (!sourceObject.getIdProducer().equals(stageObject.getIdProducer())) {
            return false;
        }
        if (!sourceObject.getName().equals(stageObject.getName())) {
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
        if (!sourceObject.getPhone().equals(stageObject.getPhone())) {
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

    public void setSourceOneMapper(SourceOneMapper sourceOneMapper) {
        this.sourceOneMapper = sourceOneMapper;
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }
}
