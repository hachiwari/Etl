package com.tkurek.wat.Etl.mapper;

import org.apache.ibatis.annotations.Param;

public interface SourceOneMapper {

    String testSelect(@Param("id") Long id);
}
