package com.tkurek.wat.Etl.mapper;

import com.tkurek.wat.Etl.model.warehouse.*;
import org.apache.ibatis.annotations.Param;

public interface WarehouseMapper {

    void insertWBrand(@Param("brand") W_Brand brand);
    void updateWBrand(@Param("brand") W_Brand brand);
    W_Brand selectLastWBrand(@Param("idBrand") Long idBrand);
    void insertWCountry(@Param("country") W_Country country);
    void updateWCountry(@Param("country") W_Country country);
    W_Country selectLastWCountry(@Param("idCountry") Long idCountry);
    void insertWProducer(@Param("producer") W_Producer producer);
    void updateWProducer(@Param("producer") W_Producer producer);
    W_Producer selectLastWProducer(@Param("idProducer") Long idProducer);
    void insertWProvider(@Param("provider") W_Provider provider);
    void updateWProvider(@Param("provider") W_Provider provider);
    W_Provider selectLastWProvider(@Param("idProvider") Long idProvider);
    void insertWTypePrice(@Param("typePrice") W_TypePrice typePrice);
    void updateWTypePrice(@Param("typePrice") W_TypePrice typePrice);
    W_TypePrice selectLastWTypePrice(@Param("idTypePrice") Long idTypePrice);
    void insertWProduct(@Param("product") W_Product product);
    void updateWProduct(@Param("product") W_Product product);
    W_Product selectLastWProduct(@Param("idProduct") Long idProduct);
    void insertWLocality(@Param("locality") W_Locality locality);
    void updateWLocality(@Param("locality") W_Locality locality);
    W_Locality selectLastWLocality(@Param("idLocality") Long idLocality);
    void insertWRegion(@Param("region") W_Region region);
    void updateWRegion(@Param("region") W_Region region);
    W_Region selectLastWRegion(@Param("idRegion") Long idRegion);
    void insertWShop(@Param("shop") W_Shop shop);
    void updateWShop(@Param("shop") W_Shop shop);
    W_Shop selectLastWShop(@Param("idShop") Long idShop);
    void insertWTypeWorker(@Param("typeWorker") W_TypeWorker typeWorker);
    void updateWTypeWorker(@Param("typeWorker") W_TypeWorker typeWorker);
    W_TypeWorker selectLastWTypeWorker(@Param("idTypeWorker") Long idTypeWorker);
    void insertWWorker(@Param("worker") W_Worker worker);
    void updateWWorker(@Param("worker") W_Worker worker);
    W_Worker selectLastWWorker(@Param("idWorker") Long idWorker);
    void insertFDelivery(@Param("delivery") F_Delivery delivery);
    void updateFDelivery(@Param("delivery") F_Delivery delivery);
    F_Delivery selectLastFDelivery(@Param("idDelivery") Long idDelivery);
    void insertFSale(@Param("sale") F_Sale sale);
    void updateFSale(@Param("sale") F_Sale sale);
    F_Sale selectLastFSale(@Param("idSale") Long idSale);

}
