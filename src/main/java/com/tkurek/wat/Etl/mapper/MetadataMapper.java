package com.tkurek.wat.Etl.mapper;

import com.tkurek.wat.Etl.model.metadata.LogImporter;
import com.tkurek.wat.Etl.model.stage.Stage_Delivery;
import com.tkurek.wat.Etl.model.stage.Stage_Sale;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Collection;

public interface MetadataMapper {

    void insertLogImporter(@Param("logImporter") LogImporter logImporter);

    Timestamp findLastTimestampForTable(String className);

    void insertBadDelivery(@Param("delivery") Stage_Delivery delivery);
    void insertBadSale(@Param("sale") Stage_Sale sale);
    Collection<Stage_Delivery> selectAllBadDelivery();
    Collection<Stage_Sale> selectAllBadSale();
    void checkAsExecutedAllBadDelivery();
    void checkAsExecutedAllBadSale();

    void cleanTable(@Param("tableName") String tableName);
}
