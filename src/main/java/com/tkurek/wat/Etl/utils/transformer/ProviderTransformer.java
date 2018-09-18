package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Producer;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Provider;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Producer;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Provider;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProviderTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(ProviderTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_Provider.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_Provider.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Provider: ", e);
        }
    }

    private void run() {
        Collection<Stage_Provider> allObjects = this.stageMapper.selectAllProvider();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_Provider.class.getSimpleName());

        Collection<Stage_Provider> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_Provider object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_Provider newObject = createNew(object);
                Tmp_W_Provider lastObject = this.stageMapper.selectLastTmpProvider(object.getIdProvider());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpProvider(lastObject);
                }

                this.stageMapper.insertTmpProvider(newObject);
                this.utilService.setStageToWarehouseId(object.getIdProvider(), newObject.getIdProvider(), Stage_Provider.class, Tmp_W_Provider.class);
            } else {
                Tmp_W_Provider lastObject = this.stageMapper.selectLastTmpProvider(object.getIdProvider());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpProvider(lastObject);
                }
            }
        }
    }

    private Tmp_W_Provider createNew(Stage_Provider object) {
        Tmp_W_Provider newObject = new Tmp_W_Provider();
        newObject.setIdProvider(object.getIdProvider());
        newObject.setIdCountry(object.getIdCountry());
        newObject.setName(object.getName());
        newObject.setAddress(object.getAddress());
        newObject.setCity(object.getCity());
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
