package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceTwoMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceTwo.SourceTwo_Region;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Region;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceTwoRegionExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SourceTwoRegionExtractor.class);

    private LogService logService;
    private UtilService utilService;
    private SourceTwoMapper sourceTwoMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceTwo_Region.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceTwo_Region.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Region: ", e);
        }
    }

    private void run() {
        Collection<SourceTwo_Region> allSources = this.sourceTwoMapper.selectAllRegion();
        Collection<Stage_Region> allStages = this.stageMapper.selectAllRegion();

        for (SourceTwo_Region object : allSources) {
            Collection<Stage_Region> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdRegion().equals(object.getIdRegion())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Region newObject = createNew(object);
                this.stageMapper.insertRegion(newObject);
                this.utilService.setSourceToStageId(object.getIdRegion(), newObject.getIdRegion(), SourceTwo_Region.class, Stage_Region.class);
            } else {
                for (Stage_Region stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Region newObject = createNew(object);
                            this.stageMapper.updateRegion(stageObject);
                            this.stageMapper.insertRegion(newObject);
                            this.utilService.setSourceToStageId(object.getIdRegion(), newObject.getIdRegion(), SourceTwo_Region.class, Stage_Region.class);
                        }
                    }
                }
            }
        }

        for (Stage_Region oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateRegion(oldObject);
            }
        }
    }

    private Stage_Region createNew(SourceTwo_Region object) {
        Stage_Region newObject = new Stage_Region();
        newObject.setIdRegion(object.getIdRegion());
        newObject.setName(object.getName());
        newObject.setCountry(object.getCountry());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceTwo_Region sourceObject, Stage_Region stageObject) {
        if (!sourceObject.getIdRegion().equals(stageObject.getIdRegion())) {
            return false;
        }
        if (!sourceObject.getName().equals(stageObject.getName())) {
            return false;
        }
        if (!sourceObject.getCountry().equals(stageObject.getCountry())) {
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
