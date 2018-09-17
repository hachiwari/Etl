package com.tkurek.wat.Etl.mapper;

import com.tkurek.wat.Etl.model.sourceTwo.*;

import java.util.Collection;

public interface SourceTwoMapper {

    Collection<SourceTwo_Locality> selectAllLocality();
    Collection<SourceTwo_Region> selectAllRegion();
    Collection<SourceTwo_Shop> selectAllShop();
    Collection<SourceTwo_TypeWorker> selectAllTypeWorker();
    Collection<SourceTwo_Worker> selectAllWorker();

}
