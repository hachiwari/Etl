package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Product;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Product;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProductTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(ProductTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_Product.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_Product.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Product: ", e);
        }
    }

    private void run() {
        Collection<Stage_Product> allObjects = this.stageMapper.selectAllProduct();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_Product.class.getSimpleName());

        Collection<Stage_Product> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_Product object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_Product newObject = createNew(object);
                Tmp_W_Product lastObject = this.stageMapper.selectLastTmpProduct(object.getIdProduct());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpProduct(lastObject);
                }

                this.stageMapper.insertTmpProduct(newObject);
                this.utilService.setStageToWarehouseId(object.getIdProduct(), newObject.getIdProduct(), Stage_Product.class, Tmp_W_Product.class);
            } else {
                Tmp_W_Product lastObject = this.stageMapper.selectLastTmpProduct(object.getIdProduct());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpProduct(lastObject);
                }
            }
        }
    }

    private Tmp_W_Product createNew(Stage_Product object) {
        Tmp_W_Product newObject = new Tmp_W_Product();
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
