package com.tkurek.wat.Etl.mapper;

import com.tkurek.wat.Etl.model.stage.SourceToStageIdMap;
import com.tkurek.wat.Etl.model.stage.StageToWarehouseIdMap;
import com.tkurek.wat.Etl.model.stage.Stage_Delivery;
import com.tkurek.wat.Etl.model.stage.Stage_Sale;
import com.tkurek.wat.Etl.model.stage.sourceOne.*;
import com.tkurek.wat.Etl.model.stage.sourceTwo.*;
import com.tkurek.wat.Etl.model.stage.tmp.*;
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
    Collection<Stage_Delivery> selectAllDelivery();
    Collection<Stage_Sale> selectAllSale();

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
    void insertDelivery(@Param("delivery") Stage_Delivery delivery);
    void updateDelivery(@Param("delivery") Stage_Delivery delivery);
    void insertSale(@Param("sale") Stage_Sale sale);
    void updateSale(@Param("sale") Stage_Sale sale);

    void insertSourceToStageIdMap(@Param("sourceToStageIdMap") SourceToStageIdMap sourceToStageIdMap);
    SourceToStageIdMap selectSourceToStageIdMap(@Param("idSource") Long idSource, @Param("sourceTableName") String sourceTableName);
    void deleteSourceToStageIdMap(@Param("sourceToStageIdMap") SourceToStageIdMap sourceToStageIdMap);
    void insertStageToWarehouseIdMap(@Param("stageToWarehouseIdMap") StageToWarehouseIdMap stageToWarehouseIdMap);
    StageToWarehouseIdMap selectStageToWarehouseIdMap(@Param("idStage") Long idStage, @Param("stageTableName") String stageTableName);

    void insertTmpBrand(@Param("brand") Tmp_W_Brand brand);
    void updateTmpBrand(@Param("brand") Tmp_W_Brand brand);
    Tmp_W_Brand selectLastTmpBrand(@Param("idBrand") Long idBrand);
    void insertTmpCountry(@Param("country") Tmp_W_Country country);
    void updateTmpCountry(@Param("country") Tmp_W_Country country);
    Tmp_W_Country selectLastTmpCountry(@Param("idCountry") Long idCountry);
    void insertTmpProducer(@Param("producer") Tmp_W_Producer producer);
    void updateTmpProducer(@Param("producer") Tmp_W_Producer producer);
    Tmp_W_Producer selectLastTmpProducer(@Param("idProducer") Long idProducer);
    void insertTmpProvider(@Param("provider") Tmp_W_Provider provider);
    void updateTmpProvider(@Param("provider") Tmp_W_Provider provider);
    Tmp_W_Provider selectLastTmpProvider(@Param("idProvider") Long idProvider);
    void insertTmpTypePrice(@Param("typePrice") Tmp_W_TypePrice typePrice);
    void updateTmpTypePrice(@Param("typePrice") Tmp_W_TypePrice typePrice);
    Tmp_W_TypePrice selectLastTmpTypePrice(@Param("idTypePrice") Long idTypePrice);
    void insertTmpProduct(@Param("product") Tmp_W_Product product);
    void updateTmpProduct(@Param("product") Tmp_W_Product product);
    Tmp_W_Product selectLastTmpProduct(@Param("idProduct") Long idProduct);
    void insertTmpLocality(@Param("locality") Tmp_W_Locality locality);
    void updateTmpLocality(@Param("locality") Tmp_W_Locality locality);
    Tmp_W_Locality selectLastTmpLocality(@Param("idLocality") Long idLocality);
    void insertTmpRegion(@Param("region") Tmp_W_Region region);
    void updateTmpRegion(@Param("region") Tmp_W_Region region);
    Tmp_W_Region selectLastTmpRegion(@Param("idRegion") Long idRegion);
    void insertTmpShop(@Param("shop") Tmp_W_Shop shop);
    void updateTmpShop(@Param("shop") Tmp_W_Shop shop);
    Tmp_W_Shop selectLastTmpShop(@Param("idShop") Long idShop);
    void insertTmpTypeWorker(@Param("typeWorker") Tmp_W_TypeWorker typeWorker);
    void updateTmpTypeWorker(@Param("typeWorker") Tmp_W_TypeWorker typeWorker);
    Tmp_W_TypeWorker selectLastTmpTypeWorker(@Param("idTypeWorker") Long idTypeWorker);
    void insertTmpWorker(@Param("worker") Tmp_W_Worker worker);
    void updateTmpWorker(@Param("worker") Tmp_W_Worker worker);
    Tmp_W_Worker selectLastTmpWorker(@Param("idWorker") Long idWorker);
    void insertTmpDelivery(@Param("delivery") Tmp_F_Delivery delivery);
    void updateTmpDelivery(@Param("delivery") Tmp_F_Delivery delivery);
    Tmp_F_Delivery selectLastTmpDelivery(@Param("idDelivery") Long idDelivery);
    void insertTmpSale(@Param("sale") Tmp_F_Sale sale);
    void updateTmpSale(@Param("sale") Tmp_F_Sale sale);
    Tmp_F_Sale selectLastTmpSale(@Param("idSale") Long idSale);

    Long selectIdProviderByName(@Param("name") String name);
    Tmp_W_Product selectProductByCode(@Param("code") String code);
    Long selectIdShopByName(@Param("name") String name);

    Collection<Tmp_W_Brand> selectAllTmpBrand();
    Collection<Tmp_W_Country> selectAllTmpCountry();
    Collection<Tmp_W_Producer> selectAllTmpProducer();
    Collection<Tmp_W_Product> selectAllTmpProduct();
    Collection<Tmp_W_Provider> selectAllTmpProvider();
    Collection<Tmp_W_TypePrice> selectAllTmpTypePrice();
    Collection<Tmp_W_Locality> selectAllTmpLocality();
    Collection<Tmp_W_Region> selectAllTmpRegion();
    Collection<Tmp_W_Shop> selectAllTmpShop();
    Collection<Tmp_W_TypeWorker> selectAllTmpTypeWorker();
    Collection<Tmp_W_Worker> selectAllTmpWorker();
    Collection<Tmp_F_Delivery> selectAllTmpDelivery();
    Collection<Tmp_F_Sale> selectAllTmpSale();

}
