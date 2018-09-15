package com.tkurek.wat.Etl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TwoMapper {

    @Select("select m.name from Mark as m where m.ID_MARK = 1")
    String getVersion();

}
