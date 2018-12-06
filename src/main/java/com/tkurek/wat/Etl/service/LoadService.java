package com.tkurek.wat.Etl.service;

import com.tkurek.wat.Etl.model.warehouse.F_Delivery;
import com.tkurek.wat.Etl.model.warehouse.F_Sale;

import java.util.Collection;

public interface LoadService {

    void load();

    Collection<F_Delivery> getAllDeliveries();
    Collection<F_Sale> getAllSales();
}
