package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Brand;
import com.tkurek.wat.Etl.model.warehouse.W_Brand;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class BrandLoader {

    private static final Logger LOG = LoggerFactory.getLogger(BrandLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_Brand.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_Brand.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Brand: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_Brand> allStages = this.stageMapper.selectAllTmpBrand();
        Timestamp lastImport = this.logService.getLastImportToStage(W_Brand.class.getSimpleName());

        Collection<Tmp_W_Brand> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_Brand object : allNewStages) {
            W_Brand lastObject = this.warehouseMapper.selectLastWBrand(object.getIdBrand());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWBrand(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_Brand newObject = createNew(object);
                this.warehouseMapper.insertWBrand(newObject);
            }
        }
    }

    private W_Brand createNew(Tmp_W_Brand object) {
        W_Brand newObject = new W_Brand();
        newObject.setIdBrand(object.getIdBrand());
        newObject.setIdProducer(object.getIdProducer());
        newObject.setName(object.getName());
        newObject.setSubBrand(object.getSubBrand());
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
