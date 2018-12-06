package com.tkurek.wat.Etl.service;

import com.tkurek.wat.Etl.model.sourceCsv.SourceDelivery;
import com.tkurek.wat.Etl.model.sourceCsv.SourceSale;

import java.util.List;

public interface ExtractService {

    void extract();

    List<SourceDelivery> extractSourceCsvDelivery();
    List<SourceSale> extractSourceCsvSale();
}
