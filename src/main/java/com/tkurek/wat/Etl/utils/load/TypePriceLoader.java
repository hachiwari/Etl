package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_TypePrice;
import com.tkurek.wat.Etl.model.warehouse.W_TypePrice;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class TypePriceLoader {

    private static final Logger LOG = LoggerFactory.getLogger(TypePriceLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_TypePrice.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_TypePrice.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load TypePrice: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_TypePrice> allStages = this.stageMapper.selectAllTmpTypePrice();
        Timestamp lastImport = this.logService.getLastImportToStage(W_TypePrice.class.getSimpleName());

        Collection<Tmp_W_TypePrice> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_TypePrice object : allNewStages) {
            W_TypePrice lastObject = this.warehouseMapper.selectLastWTypePrice(object.getIdTypePrice());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWTypePrice(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_TypePrice newObject = createNew(object);
                this.warehouseMapper.insertWTypePrice(newObject);
            }
        }
    }

    private W_TypePrice createNew(Tmp_W_TypePrice object) {
        W_TypePrice newObject = new W_TypePrice();
        newObject.setIdTypePrice(object.getIdTypePrice());
        newObject.setName(object.getName());
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
