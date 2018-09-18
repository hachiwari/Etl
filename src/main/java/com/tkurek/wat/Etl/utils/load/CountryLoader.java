package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Country;
import com.tkurek.wat.Etl.model.warehouse.W_Country;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class CountryLoader {

    private static final Logger LOG = LoggerFactory.getLogger(CountryLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_Country.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_Country.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Country: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_Country> allStages = this.stageMapper.selectAllTmpCountry();
        Timestamp lastImport = this.logService.getLastImportToStage(W_Country.class.getSimpleName());

        Collection<Tmp_W_Country> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_Country object : allNewStages) {
            W_Country lastObject = this.warehouseMapper.selectLastWCountry(object.getIdCountry());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWCountry(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_Country newObject = createNew(object);
                this.warehouseMapper.insertWCountry(newObject);
            }
        }
    }

    private W_Country createNew(Tmp_W_Country object) {
        W_Country newObject = new W_Country();
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

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }

    public void setWarehouseMapper(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }
}
