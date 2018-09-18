package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceOneMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceOne.SourceOne_Country;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Country;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceOneCountryExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SourceOneCountryExtractor.class);

    private LogService logService;
    private UtilService utilService;
    private SourceOneMapper sourceOneMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceOne_Country.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceOne_Country.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Country: ", e);
        }
    }

    private void run() {
        Collection<SourceOne_Country> allSources = this.sourceOneMapper.selectAllCountry();
        Collection<Stage_Country> allStages = this.stageMapper.selectAllCountry();

        for (SourceOne_Country object : allSources) {
            Collection<Stage_Country> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdCountry().equals(object.getIdCountry())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Country newObject = createNew(object);
                this.stageMapper.insertCountry(newObject);
                this.utilService.setSourceToStageId(object.getIdCountry(), newObject.getIdCountry(), SourceOne_Country.class, Stage_Country.class);
            } else {
                for (Stage_Country stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Country newObject = createNew(object);
                            this.stageMapper.updateCountry(stageObject);
                            this.stageMapper.insertCountry(newObject);
                            this.utilService.setSourceToStageId(object.getIdCountry(), newObject.getIdCountry(), SourceOne_Country.class, Stage_Country.class);
                        }
                    }
                }
            }
        }

        for (Stage_Country oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateCountry(oldObject);
            }
        }
    }

    private Stage_Country createNew(SourceOne_Country object) {
        Stage_Country newObject = new Stage_Country();
        newObject.setIdCountry(object.getIdCountry());
        newObject.setName(object.getName());
        newObject.setCode(object.getCode());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceOne_Country sourceObject, Stage_Country stageObject) {
        if (!sourceObject.getIdCountry().equals(stageObject.getIdCountry())) {
            return false;
        }
        if (!sourceObject.getName().equals(stageObject.getName())) {
            return false;
        }
        if (!sourceObject.getCode().equals(stageObject.getCode())) {
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
