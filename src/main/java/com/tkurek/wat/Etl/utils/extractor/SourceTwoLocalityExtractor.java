package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceTwoMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceTwo.SourceTwo_Locality;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Locality;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceTwoLocalityExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SourceTwoLocalityExtractor.class);

    private LogService logService;
    private UtilService utilService;
    private SourceTwoMapper sourceTwoMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceTwo_Locality.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceTwo_Locality.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Locality: ", e);
        }
    }

    private void run() {
        Collection<SourceTwo_Locality> allSources = this.sourceTwoMapper.selectAllLocality();
        Collection<Stage_Locality> allStages = this.stageMapper.selectAllLocality();

        for (SourceTwo_Locality object : allSources) {
            Collection<Stage_Locality> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdLocality().equals(object.getIdLocality())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Locality newObject = createNew(object);
                this.stageMapper.insertLocality(newObject);
                this.utilService.setSourceToStageId(object.getIdLocality(), newObject.getIdLocality(), SourceTwo_Locality.class, Stage_Locality.class);
            } else {
                for (Stage_Locality stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Locality newObject = createNew(object);
                            this.stageMapper.updateLocality(stageObject);
                            this.stageMapper.insertLocality(newObject);
                            this.utilService.setSourceToStageId(object.getIdLocality(), newObject.getIdLocality(), SourceTwo_Locality.class, Stage_Locality.class);
                        }
                    }
                }
            }
        }

        for (Stage_Locality oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateLocality(oldObject);
            }
        }
    }

    private Stage_Locality createNew(SourceTwo_Locality object) {
        Stage_Locality newObject = new Stage_Locality();
        newObject.setIdLocality(object.getIdLocality());
        newObject.setIdRegion(object.getIdRegion());
        newObject.setName(object.getName());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceTwo_Locality sourceObject, Stage_Locality stageObject) {
        if (!sourceObject.getIdLocality().equals(stageObject.getIdLocality())) {
            return false;
        }
        if (!sourceObject.getName().equals(stageObject.getName())) {
            return false;
        }
        return sourceObject.getIdRegion().equals(stageObject.getIdRegion());
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
