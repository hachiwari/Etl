package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.MetadataMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.Stage_Delivery;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_F_Delivery;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Product;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class DeliveryTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveryTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;
    private MetadataMapper metadataMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_F_Delivery.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_F_Delivery.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Delivery: ", e);
        }
    }

    private void run() {
        Collection<Stage_Delivery> allObjects = this.stageMapper.selectAllDelivery();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_F_Delivery.class.getSimpleName());

        Collection<Stage_Delivery> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        Collection<Stage_Delivery> badDeliveries = this.metadataMapper.selectAllBadDelivery();
        this.metadataMapper.checkAsExecutedAllBadDelivery();
        allNewObjects.addAll(badDeliveries);

        for(Stage_Delivery object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_F_Delivery newObject = createNew(object);

                if (newObject == null) {
                    this.metadataMapper.insertBadDelivery(object);
                } else {
                    Tmp_F_Delivery lastObject = this.stageMapper.selectLastTmpDelivery(object.getIdDelivery());

                    if (lastObject != null) {
                        lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                        this.stageMapper.updateTmpDelivery(lastObject);
                    }

                    this.stageMapper.insertTmpDelivery(newObject);
                    this.utilService.setStageToWarehouseId(object.getIdDelivery(), newObject.getIdDelivery(), Stage_Delivery.class, Tmp_F_Delivery.class);
                }
            } else {
                Tmp_F_Delivery lastObject = this.stageMapper.selectLastTmpDelivery(object.getIdDelivery());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpDelivery(lastObject);
                }
            }
        }
    }

    private Tmp_F_Delivery createNew(Stage_Delivery object) {
        Tmp_F_Delivery newObject = new Tmp_F_Delivery();
        Long idProvider = this.stageMapper.selectIdProviderByName(object.getNameProvider());
        Tmp_W_Product product = this.stageMapper.selectProductByCode(object.getCodeProduct());

        if (idProvider == null || product == null) {
            return null;
        }

        BigDecimal quantity = product.getPrice().multiply(new BigDecimal(object.getQuantityProduct()));
        newObject.setIdDelivery(object.getIdDelivery());
        newObject.setIdProvider(idProvider);
        newObject.setIdProduct(product.getIdProduct());
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
