package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Shop;
import com.tkurek.wat.Etl.model.warehouse.W_Shop;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class ShopLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ShopLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_Shop.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_Shop.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Shop: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_Shop> allStages = this.stageMapper.selectAllTmpShop();
        Timestamp lastImport = this.logService.getLastImportToStage(W_Shop.class.getSimpleName());

        Collection<Tmp_W_Shop> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_Shop object : allNewStages) {
            W_Shop lastObject = this.warehouseMapper.selectLastWShop(object.getIdShop());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWShop(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_Shop newObject = createNew(object);
                this.warehouseMapper.insertWShop(newObject);
            }
        }
    }

    private W_Shop createNew(Tmp_W_Shop object) {
        W_Shop newObject = new W_Shop();
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

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }

    public void setWarehouseMapper(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }
}
