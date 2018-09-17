package com.tkurek.wat.Etl.mapper;

import org.apache.ibatis.annotations.Param;

public interface WarehouseMapper {

    String testSelect2(@Param("id") Long id);

}
