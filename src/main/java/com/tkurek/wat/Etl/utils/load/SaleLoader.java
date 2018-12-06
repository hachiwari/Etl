package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_F_Sale;
import com.tkurek.wat.Etl.model.warehouse.F_Delivery;
import com.tkurek.wat.Etl.model.warehouse.F_Sale;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SaleLoader {

    private static final Logger LOG = LoggerFactory.getLogger(SaleLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(F_Sale.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(F_Sale.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Sale: ", e);
        }
    }

    public Collection<F_Sale> getAllSales() {
        return warehouseMapper.selectAllFSales();
    }

    private void run() {
        Collection<Tmp_F_Sale> allStages = this.stageMapper.selectAllTmpSale();
        Timestamp lastImport = this.logService.getLastImportToStage(F_Sale.class.getSimpleName());

        Collection<Tmp_F_Sale> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_F_Sale object : allNewStages) {
            F_Sale lastObject = this.warehouseMapper.selectLastFSale(object.getIdSale());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateFSale(lastObject);
            }

            if (object.getTimestampTo() == null) {
                F_Sale newObject = createNew(object);
                this.warehouseMapper.insertFSale(newObject);
            }
        }
    }

    private F_Sale createNew(Tmp_F_Sale object) {
        F_Sale newObject = new F_Sale();
        newObject.setIdSale(object.getIdSale());
        newObject.setIdProduct(object.getIdProduct());
        newObject.setIdShop(object.getIdShop());
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
