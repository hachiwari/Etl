package com.tkurek.wat.Etl.mapper;

import com.tkurek.wat.Etl.model.metadata.LogImporter;
import org.apache.ibatis.annotations.Param;

public interface MetadataMapper {

    void insertLogImporter(@Param("logImporter") LogImporter logImporter);

}
