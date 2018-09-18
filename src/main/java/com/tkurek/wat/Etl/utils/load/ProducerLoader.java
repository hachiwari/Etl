package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Producer;
import com.tkurek.wat.Etl.model.warehouse.W_Producer;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProducerLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_Producer.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_Producer.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Producer: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_Producer> allStages = this.stageMapper.selectAllTmpProducer();
        Timestamp lastImport = this.logService.getLastImportToStage(W_Producer.class.getSimpleName());

        Collection<Tmp_W_Producer> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_Producer object : allNewStages) {
            W_Producer lastObject = this.warehouseMapper.selectLastWProducer(object.getIdProducer());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWProducer(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_Producer newObject = createNew(object);
                this.warehouseMapper.insertWProducer(newObject);
            }
        }
    }

    private W_Producer createNew(Tmp_W_Producer object) {
        W_Producer newObject = new W_Producer();
        newObject.setIdProducer(object.getIdProducer());
        newObject.setName(object.getName());
        newObject.setAddress(object.getAddress());
        newObject.setCity(object.getCity());
        newObject.setZipCode(object.getZipCode());
        newObject.setPhone(object.getPhone());
        newObject.setTimestampFrom(object.getTimestampFrom());
        newObject.setTimestampTo(object.getTimestampTo());
        return newObject;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }

    public void setWarehouseMapper(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }
}
