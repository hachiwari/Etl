package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceOneMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceOne.SourceOne_Product;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Product;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import com.tkurek.wat.Etl.service.impl.ExtractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceOneProductExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractServiceImpl.class);

    private LogService logService;
    private UtilService utilService;
    private SourceOneMapper sourceOneMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceOne_Product.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceOne_Product.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Product: ", e);
        }
    }

    private void run() {
        Collection<SourceOne_Product> allSources = this.sourceOneMapper.selectAllProduct();
        Collection<Stage_Product> allStages = this.stageMapper.selectAllProduct();

        for (SourceOne_Product object : allSources) {
            Collection<Stage_Product> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdProduct().equals(object.getIdProduct())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Product newObject = createNew(object);
                this.stageMapper.insertProduct(newObject);
                this.utilService.setSourceToStageId(object.getIdProduct(), newObject.getIdProduct(), SourceOne_Product.class, Stage_Product.class);
            } else {
                for (Stage_Product stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Product newObject = createNew(object);
                            this.stageMapper.updateProduct(stageObject);
                            this.stageMapper.insertProduct(newObject);
                            this.utilService.setSourceToStageId(object.getIdProduct(), newObject.getIdProduct(), SourceOne_Product.class, Stage_Product.class);
                        }
                    }
                }
            }
        }

        for (Stage_Product oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateProduct(oldObject);
            }
        }
    }

    private Stage_Product createNew(SourceOne_Product object) {
        Stage_Product newObject = new Stage_Product();
        newObject.setIdProduct(object.getIdProduct());
        newObject.setIdBrand(object.getIdBrand());
        newObject.setName(object.getName());
        newObject.setCategory(object.getCategory());
        newObject.setType(object.getType());
        newObject.setPrice(object.getPrice());
        newObject.setIdTypePrice(object.getIdTypePrice());
        newObject.setQuantity(object.getQuantity());
        newObject.setDescription(object.getDescription());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceOne_Product sourceObject, Stage_Product stageObject) {
        if (!sourceObject.getIdProduct().equals(stageObject.getIdProduct())) {
            return false;
        }
        if (!sourceObject.getIdBrand().equals(stageObject.getIdBrand())) {
            return false;
        }
        if (!sourceObject.getName().equals(stageObject.getName())) {
            return false;
        }
        if (!sourceObject.getCategory().equals(stageObject.getCategory())) {
            return false;
        }
        if (!sourceObject.getType().equals(stageObject.getType())) {
            return false;
        }
        if (!sourceObject.getPrice().equals(stageObject.getPrice())) {
            return false;
        }
        if (!sourceObject.getIdTypePrice().equals(stageObject.getIdTypePrice())) {
            return false;
        }
        if (!sourceObject.getQuantity().equals(stageObject.getQuantity())) {
            return false;
        }
        if (!sourceObject.getDescription().equals(stageObject.getDescription())) {
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

    public void setSourceOneMapper(SourceOneMapper sourceOneMapper) {
        this.sourceOneMapper = sourceOneMapper;
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }
}
