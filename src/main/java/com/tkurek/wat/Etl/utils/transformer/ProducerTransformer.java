package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Producer;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Producer;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProducerTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_Producer.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_Producer.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Producer: ", e);
        }
    }

    private void run() {
        Collection<Stage_Producer> allObjects = this.stageMapper.selectAllProducer();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_Producer.class.getSimpleName());

        Collection<Stage_Producer> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_Producer object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_Producer newObject = createNew(object);
                Tmp_W_Producer lastObject = this.stageMapper.selectLastTmpProducer(object.getIdProducer());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpProducer(lastObject);
                }

                this.stageMapper.insertTmpProducer(newObject);
                this.utilService.setStageToWarehouseId(object.getIdProducer(), newObject.getIdProducer(), Stage_Producer.class, Tmp_W_Producer.class);
            } else {
                Tmp_W_Producer lastObject = this.stageMapper.selectLastTmpProducer(object.getIdProducer());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpProducer(lastObject);
                }
            }
        }
    }

    private Tmp_W_Producer createNew(Stage_Producer object) {
        Tmp_W_Producer newObject = new Tmp_W_Producer();
        newObject.setIdProducer(object.getIdProducer());
        newObject.setName(object.getName());
        newObject.setAddress(object.getAddress());
        newObject.setCity(object.getCity());
        newObject.setZipCode(object.getZipCode());
        newObject.setPhone(object.getPhone());
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
