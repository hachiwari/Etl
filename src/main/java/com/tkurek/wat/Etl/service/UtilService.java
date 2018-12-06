package com.tkurek.wat.Etl.service;

import java.io.File;
import java.util.List;

public interface UtilService {

    void setSourceToStageId(Long idSource, Long idStage, Class classSource, Class classStage);
    void setStageToWarehouseId(Long idStage, Long idWarehouse, Class classStage, Class classWarehouse);

    File[] getResourceDirectoryFiles(String directory);
    List<File> getResourceDirectoryFilesWithPrefix(String directory, String filePrefix);

    File[] getDirectoryFiles(String directory);
    List<File> getDirectoryFilesWithPrefix(String directory, String filePrefix);
}
