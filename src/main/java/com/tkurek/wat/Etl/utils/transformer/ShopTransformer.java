package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Shop;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Shop;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class ShopTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(ShopTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_Shop.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_Shop.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Shop: ", e);
        }
    }

    private void run() {
        Collection<Stage_Shop> allObjects = this.stageMapper.selectAllShop();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_Shop.class.getSimpleName());

        Collection<Stage_Shop> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_Shop object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_Shop newObject = createNew(object);
                Tmp_W_Shop lastObject = this.stageMapper.selectLastTmpShop(object.getIdShop());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpShop(lastObject);
                }

                this.stageMapper.insertTmpShop(newObject);
                this.utilService.setStageToWarehouseId(object.getIdShop(), newObject.getIdShop(), Stage_Shop.class, Tmp_W_Shop.class);
            } else {
                Tmp_W_Shop lastObject = this.stageMapper.selectLastTmpShop(object.getIdShop());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpShop(lastObject);
                }
            }
        }
    }

    private Tmp_W_Shop createNew(Stage_Shop object) {
        Tmp_W_Shop newObject = new Tmp_W_Shop();
        newObject.setIdShop(object.getIdShop());
        newObject.setIdLocality(object.getIdLocality());
        newObject.setName(object.getName());
        newObject.setPhone(object.getPhone());
        newObject.setAddress(object.getAddress());
        newObject.setZipCode(object.getZipCode());
        newObject.setPhone(object.getPhone());
        newObject.setTimestampFrom(object.getTimestampFrom());
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
