package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceOne.SourceOne_Brand;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Brand;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Brand;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class BrandTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(BrandTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_Brand.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_Brand.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Brand: ", e);
        }
    }

    private void run() {
        Collection<Stage_Brand> allObjects = this.stageMapper.selectAllBrand();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_Brand.class.getSimpleName());

        Collection<Stage_Brand> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_Brand object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_Brand newObject = createNew(object);
                Tmp_W_Brand lastObject = this.stageMapper.selectLastTmpBrand(object.getIdBrand());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpBrand(lastObject);
                }

                this.stageMapper.insertTmpBrand(newObject);
                this.utilService.setStageToWarehouseId(object.getIdBrand(), newObject.getIdBrand(), Stage_Brand.class, Tmp_W_Brand.class);
            } else {
                Tmp_W_Brand lastObject = this.stageMapper.selectLastTmpBrand(object.getIdBrand());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpBrand(lastObject);
                }
            }
        }
    }

    private Tmp_W_Brand createNew(Stage_Brand object) {
        Tmp_W_Brand newObject = new Tmp_W_Brand();
        newObject.setIdBrand(object.getIdBrand());
        newObject.setIdProducer(object.getIdProducer());
        newObject.setName(object.getName());
        newObject.setSubBrand(object.getSubBrand());
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
