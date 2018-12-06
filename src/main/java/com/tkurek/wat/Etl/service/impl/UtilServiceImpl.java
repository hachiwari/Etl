package com.tkurek.wat.Etl.service.impl;

import com.tkurek.wat.Etl.mapper.StageMapper;
import com.tkurek.wat.Etl.model.stage.SourceToStageIdMap;
import com.tkurek.wat.Etl.model.stage.StageToWarehouseIdMap;
import com.tkurek.wat.Etl.service.UtilService;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class UtilServiceImpl implements UtilService {

    private StageMapper stageMapper;

    @Override
    public void setSourceToStageId(Long idSource, Long idStage, Class classSource, Class classStage) {
        SourceToStageIdMap oldRecord = this.stageMapper.selectSourceToStageIdMap(idSource, classSource.getSimpleName());

        if (oldRecord != null && oldRecord.getIdStage().equals(idStage) && oldRecord.getStageTableName().equals(classStage.getSimpleName())) {
            return;
        }

        SourceToStageIdMap newRecord = new SourceToStageIdMap();
        newRecord.setIdSource(idSource);
        newRecord.setIdStage(idStage);
        newRecord.setSourceTableName(classSource.getSimpleName());
        newRecord.setStageTableName(classStage.getSimpleName());
        this.stageMapper.insertSourceToStageIdMap(newRecord);
    }

    @Override
    public void setStageToWarehouseId(Long idStage, Long idWarehouse, Class classStage, Class classWarehouse) {
        StageToWarehouseIdMap oldRecord = this.stageMapper.selectStageToWarehouseIdMap(idStage, classStage.getSimpleName());

        if (oldRecord != null && oldRecord.getIdWarehouse().equals(idWarehouse) && oldRecord.getWarehouseTableName().equals(classWarehouse.getSimpleName())) {
            return;
        }

        StageToWarehouseIdMap newRecord = new StageToWarehouseIdMap();
        newRecord.setIdStage(idStage);
        newRecord.setIdWarehouse(idWarehouse);
        newRecord.setStageTableName(classStage.getSimpleName());
        newRecord.setWarehouseTableName(classWarehouse.getSimpleName());
        this.stageMapper.insertStageToWarehouseIdMap(newRecord);
    }

    @Override
    public File[] getResourceDirectoryFiles(String directory) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(directory);
        String path = Objects.requireNonNull(url).getPath();
        return new File(path).listFiles();
    }

    @Override
    public List<File> getResourceDirectoryFilesWithPrefix(String directory, String filePrefix) {
        File[] files = getResourceDirectoryFiles(directory);

        List<File> filesWithPrefix = new LinkedList<>();
        for(File file : files) {
            if (file.getName().startsWith(filePrefix)) {
                filesWithPrefix.add(file);
            }
        }
        return filesWithPrefix;
    }

    @Override
    public File[] getDirectoryFiles(String directory) {
        File folder = new File(directory);
        return folder.listFiles();
    }

    @Override
    public List<File> getDirectoryFilesWithPrefix(String directory, String filePrefix) {
        File[] files = getDirectoryFiles(directory);

        List<File> filesWithPrefix = new LinkedList<>();
        for(File file : files) {
            if (file.getName().startsWith(filePrefix)) {
                filesWithPrefix.add(file);
            }
        }
        return filesWithPrefix;
    }

    public void setStageMapper(StageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }
}
