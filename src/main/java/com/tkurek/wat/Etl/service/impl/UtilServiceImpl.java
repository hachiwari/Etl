package com.tkurek.wat.Etl.service.impl;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.SourceToStageIdMap;
import com.tkurek.wat.Etl.service.UtilService;

public class UtilServiceImpl implements UtilService {

    private StageMapper stageMapper;

    @Override
    public void setSourceToStageId(Long idSource, Long idStage, Class classSource, Class classStage) {
        SourceToStageIdMap oldRecord = this.stageMapper.selectSourceToStageIdMap(idSource, classSource.getSimpleName());

//        if (oldRecord != null) {
//            this.stageMapper.deleteSourceToStageIdMap(oldRecord);
//        }

        if (!oldRecord.getIdStage().equals(idStage) || !oldRecord.getStageTableName().equals(classStage.getSimpleName())) {
            SourceToStageIdMap newRecord = new SourceToStageIdMap();
            newRecord.setIdSource(idSource);
            newRecord.setIdStage(idStage);
            newRecord.setSourceTableName(classSource.getSimpleName());
            newRecord.setStageTableName(classStage.getSimpleName());
            this.stageMapper.insertSourceToStageIdMap(newRecord);
        }
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }
}
