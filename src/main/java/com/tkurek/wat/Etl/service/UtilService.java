package com.tkurek.wat.Etl.service;

import java.io.File;
import java.util.List;

public interface UtilService {

    void setSourceToStageId(Long idSource, Long idStage, Class classSource, Class classStage);
    void setStageToWarehouseId(Long idStage, Long idWarehouse, Class classStage, Class classWarehouse);

    File[] getResourceFolderFiles(String folder);
    List<File> getResourceFolderFilesWithPrefix(String folder, String filePrefix);
}
