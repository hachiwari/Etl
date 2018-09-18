package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceCsv.SourceSale;
import com.tkurek.wat.Etl.model.stage.Stage_Sale;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SourceSaleExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SourceSaleExtractor.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void extract(List<SourceSale> allSources) {
        try {
            run(allSources);
            this.logService.logImport(Stage_Sale.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Stage_Sale.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Sale: ", e);
        }
    }

    private void run(List<SourceSale> allSources) {
        Collection<Stage_Sale> allStages = this.stageMapper.selectAllSale();

        for (SourceSale object : allSources) {
            Collection<Stage_Sale> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdSale().equals(object.getIdSale())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Sale newObject = createNew(object);
                this.stageMapper.insertSale(newObject);
                this.utilService.setSourceToStageId(object.getIdSale(), newObject.getIdSale(), SourceSale.class, Stage_Sale.class);
            } else {
                for (Stage_Sale stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Sale newObject = createNew(object);
                            this.stageMapper.updateSale(stageObject);
                            this.stageMapper.insertSale(newObject);
                            this.utilService.setSourceToStageId(object.getIdSale(), newObject.getIdSale(), SourceSale.class, Stage_Sale.class);
                        }
                    }
                }
            }
        }

        for (Stage_Sale oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateSale(oldObject);
            }
        }
    }

    private Stage_Sale createNew(SourceSale object) {
        Stage_Sale newObject = new Stage_Sale();
        newObject.setIdSale(object.getIdSale());
        newObject.setCodeProduct(object.getCodeProduct());
        newObject.setNameShop(object.getNameShop());
        newObject.setQuantityProduct(object.getQuantityProduct());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceSale sourceObject, Stage_Sale stageObject) {
        if (!sourceObject.getIdSale().equals(stageObject.getIdSale())) {
            return false;
        }
        if (!sourceObject.getCodeProduct().equals(stageObject.getCodeProduct())) {
            return false;
        }
        if (!sourceObject.getNameShop().equals(stageObject.getNameShop())) {
            return false;
        }
        if (!sourceObject.getQuantityProduct().equals(stageObject.getQuantityProduct())) {
            return false;
        }

        return true;
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
