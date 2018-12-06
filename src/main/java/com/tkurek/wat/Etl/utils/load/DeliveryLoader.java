package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_F_Delivery;
import com.tkurek.wat.Etl.model.warehouse.F_Delivery;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class DeliveryLoader {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveryLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(F_Delivery.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(F_Delivery.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Delivery: ", e);
        }
    }

    public Collection<F_Delivery> getAllDeliveries() {
        return warehouseMapper.selectAllFDeliveries();
    }

    private void run() {
        Collection<Tmp_F_Delivery> allStages = this.stageMapper.selectAllTmpDelivery();
        Timestamp lastImport = this.logService.getLastImportToStage(F_Delivery.class.getSimpleName());

        Collection<Tmp_F_Delivery> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for (Tmp_F_Delivery object : allNewStages) {
            F_Delivery lastObject = this.warehouseMapper.selectLastFDelivery(object.getIdDelivery());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateFDelivery(lastObject);
            }

            if (object.getTimestampTo() == null) {
                F_Delivery newObject = createNew(object);
                this.warehouseMapper.insertFDelivery(newObject);
            }
        }
    }

    private F_Delivery createNew(Tmp_F_Delivery object) {
        F_Delivery newObject = new F_Delivery();
        newObject.setIdDelivery(object.getIdDelivery());
        newObject.setIdProvider(object.getIdProvider());
        newObject.setIdProduct(object.getIdProduct());
        newObject.setQuantityProduct(object.getQuantityProduct());
        newObject.setPrice(object.getPrice());
        newObject.setIdTypePrice(object.getIdTypePrice());
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
