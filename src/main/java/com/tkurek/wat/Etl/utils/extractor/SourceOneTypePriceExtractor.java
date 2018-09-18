package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceOneMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceOne.SourceOne_TypePrice;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_TypePrice;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceOneTypePriceExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SourceOneTypePriceExtractor.class);

    private LogService logService;
    private UtilService utilService;
    private SourceOneMapper sourceOneMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceOne_TypePrice.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceOne_TypePrice.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting TypePrice: ", e);
        }
    }

    private void run() {
        Collection<SourceOne_TypePrice> allSources = this.sourceOneMapper.selectAllTypePrice();
        Collection<Stage_TypePrice> allStages = this.stageMapper.selectAllTypePrice();

        for (SourceOne_TypePrice object : allSources) {
            Collection<Stage_TypePrice> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdTypePrice().equals(object.getIdTypePrice())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_TypePrice newObject = createNew(object);
                this.stageMapper.insertTypePrice(newObject);
                this.utilService.setSourceToStageId(object.getIdTypePrice(), newObject.getIdTypePrice(), SourceOne_TypePrice.class, Stage_TypePrice.class);
            } else {
                for (Stage_TypePrice stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_TypePrice newObject = createNew(object);
                            this.stageMapper.updateTypePrice(stageObject);
                            this.stageMapper.insertTypePrice(newObject);
                            this.utilService.setSourceToStageId(object.getIdTypePrice(), newObject.getIdTypePrice(), SourceOne_TypePrice.class, Stage_TypePrice.class);
                        }
                    }
                }
            }
        }

        for (Stage_TypePrice oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateTypePrice(oldObject);
            }
        }
    }

    private Stage_TypePrice createNew(SourceOne_TypePrice object) {
        Stage_TypePrice newObject = new Stage_TypePrice();
        newObject.setIdTypePrice(object.getIdTypePrice());
        newObject.setName(object.getName());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceOne_TypePrice sourceObject, Stage_TypePrice stageObject) {
        if (!sourceObject.getIdTypePrice().equals(stageObject.getIdTypePrice())) {
            return false;
        }
        if (!sourceObject.getName().equals(stageObject.getName())) {
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
