package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Producer;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Product;
import com.tkurek.wat.Etl.model.warehouse.W_Producer;
import com.tkurek.wat.Etl.model.warehouse.W_Product;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProductLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ProductLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_Product.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_Product.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Product: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_Product> allStages = this.stageMapper.selectAllTmpProduct();
        Timestamp lastImport = this.logService.getLastImportToStage(W_Product.class.getSimpleName());

        Collection<Tmp_W_Product> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_Product object : allNewStages) {
            W_Product lastObject = this.warehouseMapper.selectLastWProduct(object.getIdProduct());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWProduct(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_Product newObject = createNew(object);
                this.warehouseMapper.insertWProduct(newObject);
            }
        }
    }

    private W_Product createNew(Tmp_W_Product object) {
        W_Product newObject = new W_Product();
        newObject.setIdProduct(object.getIdProduct());
        newObject.setIdBrand(object.getIdBrand());
        newObject.setName(object.getName());
        newObject.setCode(object.getCode());
        newObject.setCategory(object.getCategory());
        newObject.setType(object.getType());
        newObject.setPrice(object.getPrice());
        newObject.setIdTypePrice(object.getIdTypePrice());
        newObject.setQuantity(object.getQuantity());
        newObject.setDescription(object.getDescription());
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
