package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceTwoMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceCsv.SourceDelivery;
import com.tkurek.wat.Etl.model.sourceTwo.SourceTwo_Worker;
import com.tkurek.wat.Etl.model.stage.Stage_Delivery;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Worker;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SourceDeliveryExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SourceDeliveryExtractor.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void extract(List<SourceDelivery> allSources) {
        try {
            run(allSources);
            this.logService.logImport(Stage_Delivery.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Stage_Delivery.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Delivery: ", e);
        }
    }

    private void run(List<SourceDelivery> allSources) {
        Collection<Stage_Delivery> allStages = this.stageMapper.selectAllDelivery();

        for (SourceDelivery object : allSources) {
            Collection<Stage_Delivery> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdDelivery().equals(object.getIdDelivery())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Delivery newObject = createNew(object);
                this.stageMapper.insertDelivery(newObject);
                this.utilService.setSourceToStageId(object.getIdDelivery(), newObject.getIdDelivery(), SourceDelivery.class, Stage_Delivery.class);
            } else {
                for (Stage_Delivery stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Delivery newObject = createNew(object);
                            this.stageMapper.updateDelivery(stageObject);
                            this.stageMapper.insertDelivery(newObject);
                            this.utilService.setSourceToStageId(object.getIdDelivery(), newObject.getIdDelivery(), SourceDelivery.class, Stage_Delivery.class);
                        }
                    }
                }
            }
        }

        for (Stage_Delivery oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateDelivery(oldObject);
            }
        }
    }

    private Stage_Delivery createNew(SourceDelivery object) {
        Stage_Delivery newObject = new Stage_Delivery();
        newObject.setIdDelivery(object.getIdDelivery());
        newObject.setNameProvider(object.getNameProvider());
        newObject.setCodeProduct(object.getCodeProduct());
        newObject.setQuantityProduct(object.getQuantityProduct());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceDelivery sourceObject, Stage_Delivery stageObject) {
        if (!sourceObject.getIdDelivery().equals(stageObject.getIdDelivery())) {
            return false;
        }
        if (!sourceObject.getNameProvider().equals(stageObject.getNameProvider())) {
            return false;
        }
        if (!sourceObject.getCodeProduct().equals(stageObject.getCodeProduct())) {
            return false;
        }
        if (!sourceObject.getQuantityProduct().equals(stageObject.getQuantityProduct())) {
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

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }
}
