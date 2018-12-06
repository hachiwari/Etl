package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Locality;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Locality;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class LocalityTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(LocalityTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_Locality.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_Locality.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Locality: ", e);
        }
    }

    private void run() {
        Collection<Stage_Locality> allObjects = this.stageMapper.selectAllLocality();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_Locality.class.getSimpleName());

        Collection<Stage_Locality> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_Locality object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_Locality newObject = createNew(object);
                Tmp_W_Locality lastObject = this.stageMapper.selectLastTmpLocality(object.getIdLocality());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpLocality(lastObject);
                }

                this.stageMapper.insertTmpLocality(newObject);
                this.utilService.setStageToWarehouseId(object.getIdLocality(), newObject.getIdLocality(), Stage_Locality.class, Tmp_W_Locality.class);
            } else {
                Tmp_W_Locality lastObject = this.stageMapper.selectLastTmpLocality(object.getIdLocality());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpLocality(lastObject);
                }
            }
        }
    }

    private Tmp_W_Locality createNew(Stage_Locality object) {
        Tmp_W_Locality newObject = new Tmp_W_Locality();
        newObject.setIdLocality(object.getIdLocality());
        newObject.setIdRegion(object.getIdRegion());
        newObject.setName(object.getName());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
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
