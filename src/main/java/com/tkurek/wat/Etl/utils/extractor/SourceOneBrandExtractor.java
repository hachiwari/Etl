package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceOneMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceOne.SourceOne_Brand;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Brand;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceOneBrandExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SourceOneBrandExtractor.class);

    private LogService logService;
    private UtilService utilService;
    private SourceOneMapper sourceOneMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceOne_Brand.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceOne_Brand.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Brand: ", e);
        }
    }

    private void run() {
        Collection<SourceOne_Brand> allSources = this.sourceOneMapper.selectAllBrand();
        Collection<Stage_Brand> allStages = this.stageMapper.selectAllBrand();

        for (SourceOne_Brand object : allSources) {
            Collection<Stage_Brand> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdBrand().equals(object.getIdBrand())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Brand newObject = createNew(object);
                this.stageMapper.insertBrand(newObject);
                this.utilService.setSourceToStageId(object.getIdBrand(), newObject.getIdBrand(), SourceOne_Brand.class, Stage_Brand.class);
            } else {
                for (Stage_Brand stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Brand newObject = createNew(object);
                            this.stageMapper.updateBrand(stageObject);
                            this.stageMapper.insertBrand(newObject);
                            this.utilService.setSourceToStageId(object.getIdBrand(), newObject.getIdBrand(), SourceOne_Brand.class, Stage_Brand.class);
                        }
                    }
                }
            }
        }

        for (Stage_Brand oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateBrand(oldObject);
            }
        }
    }

    private Stage_Brand createNew(SourceOne_Brand object) {
        Stage_Brand newObject = new Stage_Brand();
        newObject.setIdBrand(object.getIdBrand());
        newObject.setIdProducer(object.getIdProducer());
        newObject.setName(object.getName());
        newObject.setSubBrand(object.getSubBrand());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceOne_Brand sourceObject, Stage_Brand stageObject) {
        if (!sourceObject.getIdBrand().equals(stageObject.getIdBrand())) {
            return false;
        }
        if (!sourceObject.getIdProducer().equals(stageObject.getIdProducer())) {
            return false;
        }
        if (!sourceObject.getName().equals(stageObject.getName())) {
            return false;
        }
        return sourceObject.getSubBrand().equals(stageObject.getSubBrand());
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
