package com.tkurek.wat.Etl.mapper;

import com.tkurek.wat.Etl.model.stage.SourceToStageIdMap;
import com.tkurek.wat.Etl.model.stage.sourceOne.*;
import com.tkurek.wat.Etl.model.stage.sourceTwo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

public interface StageMapper {

    Collection<Stage_Brand> selectAllBrand();
    Collection<Stage_Country> selectAllCountry();
    Collection<Stage_Producer> selectAllProducer();
    Collection<Stage_Product> selectAllProduct();
    Collection<Stage_Provider> selectAllProvider();
    Collection<Stage_TypePrice> selectAllTypePrice();
    Collection<Stage_Locality> selectAllLocality();
    Collection<Stage_Region> selectAllRegion();
    Collection<Stage_Shop> selectAllShop();
    Collection<Stage_TypeWorker> selectAllTypeWorker();
    Collection<Stage_Worker> selectAllWorker();

    void insertBrand(@Param("brand") Stage_Brand brand);
    void updateBrand(@Param("brand") Stage_Brand brand);
    void insertCountry(@Param("country") Stage_Country country);
    void updateCountry(@Param("country") Stage_Country country);
    void insertProducer(@Param("producer") Stage_Producer producer);
    void updateProducer(@Param("producer") Stage_Producer producer);
    void insertProduct(@Param("product") Stage_Product product);
    void updateProduct(@Param("product") Stage_Product product);
    void insertProvider(@Param("provider") Stage_Provider provider);
    void updateProvider(@Param("provider") Stage_Provider provider);
    void insertTypePrice(@Param("typePrice") Stage_TypePrice typePrice);
    void updateTypePrice(@Param("typePrice") Stage_TypePrice typePrice);
    void insertLocality(@Param("locality") Stage_Locality locality);
    void updateLocality(@Param("locality") Stage_Locality locality);
    void insertRegion(@Param("region") Stage_Region region);
    void updateRegion(@Param("region") Stage_Region region);
    void insertShop(@Param("shop") Stage_Shop shop);
    void updateShop(@Param("shop") Stage_Shop shop);
    void insertTypeWorker(@Param("typeWorker") Stage_TypeWorker typeWorker);
    void updateTypeWorker(@Param("typeWorker") Stage_TypeWorker typeWorker);
    void insertWorker(@Param("worker") Stage_Worker worker);
    void updateWorker(@Param("worker") Stage_Worker worker);

    void insertSourceToStageIdMap(@Param("sourceToStageIdMap") SourceToStageIdMap sourceToStageIdMap);
    SourceToStageIdMap selectSourceToStageIdMap(@Param("idSource") Long idSource, @Param("sourceTableName") String sourceTableName);
    void deleteSourceToStageIdMap(@Param("sourceToStageIdMap") SourceToStageIdMap sourceToStageIdMap);

}
