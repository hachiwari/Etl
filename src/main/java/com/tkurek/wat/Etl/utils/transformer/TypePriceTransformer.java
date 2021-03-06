package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_TypePrice;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_TypePrice;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class TypePriceTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(TypePriceTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_TypePrice.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_TypePrice.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform TypePrice: ", e);
        }
    }

    private void run() {
        Collection<Stage_TypePrice> allObjects = this.stageMapper.selectAllTypePrice();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_TypePrice.class.getSimpleName());

        Collection<Stage_TypePrice> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_TypePrice object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_TypePrice newObject = createNew(object);
                Tmp_W_TypePrice lastObject = this.stageMapper.selectLastTmpTypePrice(object.getIdTypePrice());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpTypePrice(lastObject);
                }

                this.stageMapper.insertTmpTypePrice(newObject);
                this.utilService.setStageToWarehouseId(object.getIdTypePrice(), newObject.getIdTypePrice(), Stage_TypePrice.class, Tmp_W_TypePrice.class);
            } else {
                Tmp_W_TypePrice lastObject = this.stageMapper.selectLastTmpTypePrice(object.getIdTypePrice());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpTypePrice(lastObject);
                }
            }
        }
    }

    private Tmp_W_TypePrice createNew(Stage_TypePrice object) {
        Tmp_W_TypePrice newObject = new Tmp_W_TypePrice();
        newObject.setIdTypePrice(object.getIdTypePrice());
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
