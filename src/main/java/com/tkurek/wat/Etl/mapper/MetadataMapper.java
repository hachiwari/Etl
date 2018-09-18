package com.tkurek.wat.Etl.mapper;

import com.tkurek.wat.Etl.model.metadata.LogImporter;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

public interface MetadataMapper {

    void insertLogImporter(@Param("logImporter") LogImporter logImporter);

    Timestamp findLastTimestampForTable(String className);
}
