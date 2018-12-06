package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.MetadataMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.Stage_Sale;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_F_Sale;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Product;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SaleTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(SaleTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;
    private MetadataMapper metadataMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_F_Sale.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_F_Sale.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Sale: ", e);
        }
    }

    private void run() {
        Collection<Stage_Sale> allObjects = this.stageMapper.selectAllSale();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_F_Sale.class.getSimpleName());

        Collection<Stage_Sale> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        Collection<Stage_Sale> badSales = this.metadataMapper.selectAllBadSale();
        this.metadataMapper.checkAsExecutedAllBadSale();
        allNewObjects.addAll(badSales);

        for(Stage_Sale object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_F_Sale newObject = createNew(object);

                if (newObject == null) {
                    this.metadataMapper.insertBadSale(object);
                } else {

                    Tmp_F_Sale lastObject = this.stageMapper.selectLastTmpSale(object.getIdSale());

                    if (lastObject != null) {
                        lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                        this.stageMapper.updateTmpSale(lastObject);
                    }

                    this.stageMapper.insertTmpSale(newObject);
                    this.utilService.setStageToWarehouseId(object.getIdSale(), newObject.getIdSale(), Stage_Sale.class, Tmp_F_Sale.class);
                }
            } else {
                Tmp_F_Sale lastObject = this.stageMapper.selectLastTmpSale(object.getIdSale());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpSale(lastObject);
                }
            }
        }
    }

    private Tmp_F_Sale createNew(Stage_Sale object) {
        Tmp_F_Sale newObject = new Tmp_F_Sale();
        Long idShop = this.stageMapper.selectIdShopByName(object.getNameShop());
        Tmp_W_Product product = this.stageMapper.selectProductByCode(object.getCodeProduct());

        if (idShop == null || product == null) {
            return null;
        }

        BigDecimal quantity = product.getPrice().multiply(new BigDecimal(object.getQuantityProduct()));
        newObject.setIdSale(object.getIdSale());
        newObject.setIdProduct(product.getIdProduct());
        newObject.setIdShop(idShop);
        newObject.setQuantityProduct(object.getQuantityProduct());
        newObject.setPrice(quantity);
        newObject.setIdTypePrice(product.getIdTypePrice());
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

    public void setMetadataMapper(MetadataMapper metadataMapper) {
        this.metadataMapper = metadataMapper;
    }
}
