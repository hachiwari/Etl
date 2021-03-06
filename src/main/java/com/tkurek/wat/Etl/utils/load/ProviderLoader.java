package com.tkurek.wat.Etl.utils.load;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.mapper.WarehouseMapper;
import com.tkurek.wat.Etl.model.stage.tmp.Tmp_W_Provider;
import com.tkurek.wat.Etl.model.warehouse.W_Provider;
import com.tkurek.wat.Etl.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProviderLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ProviderLoader.class);

    private LogService logService;
    private StageMapper stageMapper;
    private WarehouseMapper warehouseMapper;

    public void load() {
        try {
            run();
            this.logService.logImport(W_Provider.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            this.logService.logImport(W_Provider.class.getSimpleName(), new Timestamp(System.currentTimeMillis()), false);
            LOG.error("error while load Provider: ", e);
        }
    }

    private void run() {
        Collection<Tmp_W_Provider> allStages = this.stageMapper.selectAllTmpProvider();
        Timestamp lastImport = this.logService.getLastImportToStage(W_Provider.class.getSimpleName());

        Collection<Tmp_W_Provider> allNewStages = allStages
                .stream()
                .filter(stage -> (stage.getTimestampFrom().after(lastImport) || stage.getTimestampTo() != null && stage.getTimestampTo().after(lastImport)))
                .collect(Collectors.toList());

        for(Tmp_W_Provider object : allNewStages) {
            W_Provider lastObject = this.warehouseMapper.selectLastWProvider(object.getIdProvider());

            if (lastObject != null) {
                lastObject.setTimestampTo(new Timestamp(System.currentTimeMillis()));
                this.warehouseMapper.updateWProvider(lastObject);
            }

            if (object.getTimestampTo() == null) {
                W_Provider newObject = createNew(object);
                this.warehouseMapper.insertWProvider(newObject);
            }
        }
    }

    private W_Provider createNew(Tmp_W_Provider object) {
        W_Provider newObject = new W_Provider();
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

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }

    public void setWarehouseMapper(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }
}
