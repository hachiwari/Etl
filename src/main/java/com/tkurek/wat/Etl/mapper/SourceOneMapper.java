package com.tkurek.wat.Etl.mapper;

import com.tkurek.wat.Etl.model.sourceOne.*;

import java.util.Collection;

public interface SourceOneMapper {

    Collection<SourceOne_Brand> selectAllBrand();
    Collection<SourceOne_Country> selectAllCountry();
    Collection<SourceOne_Producer> selectAllProducer();
    Collection<SourceOne_Product> selectAllProduct();
    Collection<SourceOne_Provider> selectAllProvider();
    Collection<SourceOne_TypePrice> selectAllTypePrice();

}
