package com.tkurek.wat.Etl.utils.transformer;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.sourceOne.Stage_Country;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Country;
import com.tkurek.wat.Etl.service.LogService;
import com.tkurek.wat.Etl.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class CountryTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(CountryTransformer.class);

    private LogService logService;
    private UtilService utilService;
    private StageMapper stageMapper;

    public void transform() {
        try {
            run();
            this.logService.logImport(Tmp_W_Country.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(Tmp_W_Country.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while transform Country: ", e);
        }
    }

    private void run() {
        Collection<Stage_Country> allObjects = this.stageMapper.selectAllCountry();
        Timestamp lastImport = this.logService.getLastImportToStage(Tmp_W_Country.class.getSimpleName());

        Collection<Stage_Country> allNewObjects = allObjects
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Stage_Country object : allNewObjects) {
            if (object.getTimestampTo() == null) {
                Tmp_W_Country newObject = createNew(object);
                Tmp_W_Country lastObject = this.stageMapper.selectLastTmpCountry(object.getIdCountry());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpCountry(lastObject);
                }

                this.stageMapper.insertTmpCountry(newObject);
                this.utilService.setStageToWarehouseId(object.getIdCountry(), newObject.getIdCountry(), Stage_Country.class, Tmp_W_Country.class);
            } else {
                Tmp_W_Country lastObject = this.stageMapper.selectLastTmpCountry(object.getIdCountry());

                if (lastObject != null) {
                    lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                    this.stageMapper.updateTmpCountry(lastObject);
                }
            }
        }
    }

    private Tmp_W_Country createNew(Stage_Country object) {
        Tmp_W_Country newObject = new Tmp_W_Country();
        newObject.setIdCountry(object.getIdCountry());
        newObject.setName(object.getName());
        newObject.setCode(object.getCode());
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
