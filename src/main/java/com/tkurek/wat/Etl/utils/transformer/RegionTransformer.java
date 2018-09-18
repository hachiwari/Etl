package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Locality;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Region;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Locality;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Region;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class RegionTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(RegionTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_Region.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_Region.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Region: ", e);
        }
    }

    private void run() {
        Collection<Stage_Region> allObjects = this.stageMapper.selectAllRegion();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_Region.class.getSimpleName());

        Collection<Stage_Region> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_Region object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_Region newObject = createNew(object);
                Tmp_W_Region lastObject = this.stageMapper.selectLastTmpRegion(object.getIdRegion());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpRegion(lastObject);
                }

                this.stageMapper.insertTmpRegion(newObject);
                this.utilService.setStageToWarehouseId(object.getIdRegion(), newObject.getIdRegion(), Stage_Region.class, Tmp_W_Region.class);
            } else {
                Tmp_W_Region lastObject = this.stageMapper.selectLastTmpRegion(object.getIdRegion());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpRegion(lastObject);
                }
            }
        }
    }

    private Tmp_W_Region createNew(Stage_Region object) {
        Tmp_W_Region newObject = new Tmp_W_Region();
        newObject.setIdRegion(object.getIdRegion());
        newObject.setName(object.getName());
        newObject.setCountry(object.getCountry());
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
