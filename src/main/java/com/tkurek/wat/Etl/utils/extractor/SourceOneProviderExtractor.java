package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceOneMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceOne.SourceOne_Provider;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Provider;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceOneProviderExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SourceOneProviderExtractor.class);

    private LogService logService;
    private UtilService utilService;
    private SourceOneMapper sourceOneMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceOne_Provider.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceOne_Provider.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Provider: ", e);
        }
    }

    private void run() {
        Collection<SourceOne_Provider> allSources = this.sourceOneMapper.selectAllProvider();
        Collection<Stage_Provider> allStages = this.stageMapper.selectAllProvider();

        for (SourceOne_Provider object : allSources) {
            Collection<Stage_Provider> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdProvider().equals(object.getIdProvider())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Provider newObject = createNew(object);
                this.stageMapper.insertProvider(newObject);
                this.utilService.setSourceToStageId(object.getIdProvider(), newObject.getIdProvider(), SourceOne_Provider.class, Stage_Provider.class);
            } else {
                for (Stage_Provider stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Provider newObject = createNew(object);
                            this.stageMapper.updateProvider(stageObject);
                            this.stageMapper.insertProvider(newObject);
                            this.utilService.setSourceToStageId(object.getIdProvider(), newObject.getIdProvider(), SourceOne_Provider.class, Stage_Provider.class);
                        }
                    }
                }
            }
        }

        for (Stage_Provider oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateProvider(oldObject);
            }
        }
    }

    private Stage_Provider createNew(SourceOne_Provider object) {
        Stage_Provider newObject = new Stage_Provider();
        newObject.setIdProvider(object.getIdProvider());
        newObject.setIdCountry(object.getIdCountry());
        newObject.setName(object.getName());
        newObject.setAddress(object.getAddress());
        newObject.setCity(object.getCity());
        newObject.setZipCode(object.getZipCode());
        newObject.setPhone(object.getPhone());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceOne_Provider sourceObject, Stage_Provider stageObject) {
        if (!sourceObject.getIdProvider().equals(stageObject.getIdProvider())) {
            return false;
        }
         if (!sourceObject.getIdCountry().equals(stageObject.getIdCountry())) {
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
