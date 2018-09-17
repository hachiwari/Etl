package com.tkurek.wat.Etl.utils.extractor;

import com.tkurek.wat.Etl.mapper.SourceTwoMapper;
import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.sourceOne.SourceOne_Brand;
import com.tkurek.wat.Etl.model.sourceTwo.SourceTwo_Region;
import com.tkurek.wat.Etl.model.sourceTwo.SourceTwo_Shop;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Brand;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Region;
import com.tkurek.wat.Etl.model.stage.sourceTwo.Stage_Shop;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import com.tkurek.wat.Etl.service.impl.ExtractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class SourceTwoShopExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractServiceImpl.class);

    private LogService logService;
    private UtilService utilService;
    private SourceTwoMapper sourceTwoMapper;
    private StageMapper stageMapper;

    public void extract() {
        try {
            run();
            this.logService.logImport(SourceTwo_Shop.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(SourceTwo_Shop.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while extracting Shop: ", e);
        }
    }

    private void run() {
        Collection<SourceTwo_Shop> allSources = this.sourceTwoMapper.selectAllShop();
        Collection<Stage_Shop> allStages = this.stageMapper.selectAllShop();

        for (SourceTwo_Shop object : allSources) {
            Collection<Stage_Shop> vStageObjectWithSameId = allStages.stream().filter(stageObject -> stageObject.getIdShop().equals(object.getIdShop())).collect(Collectors.toList());
            allStages.removeAll(vStageObjectWithSameId);

            if (vStageObjectWithSameId.size() == 0) {
                Stage_Shop newObject = createNew(object);
                this.stageMapper.insertShop(newObject);
                this.utilService.setSourceToStageId(object.getIdShop(), newObject.getIdShop(), SourceTwo_Shop.class, Stage_Shop.class);
            } else {
                for (Stage_Shop stageObject : vStageObjectWithSameId) {
                    if (stageObject.getTimestampTo() == null) {
                        if (!compareObject(object, stageObject)) {
                            stageObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));

                            Stage_Shop newObject = createNew(object);
                            this.stageMapper.updateShop(stageObject);
                            this.stageMapper.insertShop(newObject);
                            this.utilService.setSourceToStageId(object.getIdShop(), newObject.getIdShop(), SourceTwo_Shop.class, Stage_Shop.class);
                        }
                    }
                }
            }
        }

        for (Stage_Shop oldObject : allStages) {
            if (oldObject.getTimestampTo() == null) {
                oldObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.stageMapper.updateShop(oldObject);
            }
        }
    }

    private Stage_Shop createNew(SourceTwo_Shop object) {
        Stage_Shop newObject = new Stage_Shop();
        newObject.setIdShop(object.getIdShop());
        newObject.setIdLocality(object.getIdLocality());
        newObject.setName(object.getName());
        newObject.setPhone(object.getPhone());
        newObject.setAddress(object.getAddress());
        newObject.setZipCode(object.getZipCode());
        newObject.setTimestampFrom(new Timestamp(System.currentTimeMillis()));
        newObject.setTimestampTo(null);
        return newObject;
    }

    private boolean compareObject(SourceTwo_Shop sourceObject, Stage_Shop stageObject) {
        if (!sourceObject.getIdShop().equals(stageObject.getIdShop())) {
            return false;
        }
        if (!sourceObject.getIdLocality().equals(stageObject.getIdLocality())) {
            return false;
        }
        if (!sourceObject.getName().equals(stageObject.getName())) {
            return false;
        }
        if (!sourceObject.getPhone().equals(stageObject.getPhone())) {
            return false;
        }
        if (!sourceObject.getAddress().equals(stageObject.getAddress())) {
            return false;
        }
        if (!sourceObject.getZipCode().equals(stageObject.getZipCode())) {
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

    public void setSourceTwoMapper(SourceTwoMapper sourceTwoMapper) {
        this.sourceTwoMapper = sourceTwoMapper;
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }
}
