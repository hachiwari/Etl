package com.tkurek.wat.Etl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {

    String testSelect(@Param("id") Long id);
}
