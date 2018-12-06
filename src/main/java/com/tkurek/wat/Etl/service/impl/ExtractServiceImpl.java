package com.tkurek.wat.Etl.service.impl;

import com.tkurek.wat.Etl.model.sourceCsv.SourceSale;
import com.tkurek.wat.Etl.model.stage.Csv_Delivery;
import com.tkurek.wat.Etl.model.sourceCsv.SourceDelivery;
import com.tkurek.wat.Etl.model.stage.Csv_Sale;
import com.tkurek.wat.Etl.service.ExtractService;
import com.tkurek.wat.Etl.service.UtilService;
import com.tkurek.wat.Etl.utils.csv.CsvReader;
import com.tkurek.wat.Etl.utils.extractor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExtractServiceImpl implements ExtractService {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractServiceImpl.class);

    private UtilService utilService;
    private SourceOneBrandExtractor sourceOneBrandExtractor;
    private SourceOneCountryExtractor sourceOneCountryExtractor;
    private SourceOneProducerExtractor sourceOneProducerExtractor;
    private SourceOneProductExtractor sourceOneProductExtractor;
    private SourceOneProviderExtractor sourceOneProviderExtractor;
    private SourceOneTypePriceExtractor sourceOneTypePriceExtractor;
    private SourceTwoLocalityExtractor sourceTwoLocalityExtractor;
    private SourceTwoRegionExtractor sourceTwoRegionExtractor;
    private SourceTwoShopExtractor sourceTwoShopExtractor;
    private SourceTwoTypeWorkerExtractor sourceTwoTypeWorkerExtractor;
    private SourceTwoWorkerExtractor sourceTwoWorkerExtractor;
    private SourceDeliveryExtractor sourceDeliveryExtractor;
    private SourceSaleExtractor sourceSaleExtractor;

    @Override
    public void extract() {
        LOG.info("Init Extract");
        this.sourceOneBrandExtractor.extract();
        this.sourceOneCountryExtractor.extract();
        this.sourceOneProducerExtractor.extract();
        this.sourceOneProductExtractor.extract();
        this.sourceOneProviderExtractor.extract();
        this.sourceOneTypePriceExtractor.extract();
        this.sourceTwoLocalityExtractor.extract();
        this.sourceTwoRegionExtractor.extract();
        this.sourceTwoShopExtractor.extract();
        this.sourceTwoTypeWorkerExtractor.extract();
        this.sourceTwoWorkerExtractor.extract();

        List<SourceDelivery> sourceDeliveries = extractSourceCsvDelivery();
        this.sourceDeliveryExtractor.extract(sourceDeliveries);

        List<SourceSale> sourceSales = extractSourceCsvSale();
        this.sourceSaleExtractor.extract(sourceSales);
        LOG.info("Done Extract");
    }

    @Override
    public List<SourceDelivery> extractSourceCsvDelivery() {
        List<File> deliveryFiles = utilService.getDirectoryFilesWithPrefix("csv", "Delivery_");
        List<SourceDelivery> sourceDeliveries = new LinkedList<>();
        for(File file : deliveryFiles) {
            List<Csv_Delivery> csvDeliveries = extractDeliveryCsv(file.getPath());

            for(Csv_Delivery csvDelivery : csvDeliveries) {
                SourceDelivery newObject = createDelivery(csvDelivery);
                sourceDeliveries.add(newObject);
            }
        }
        return sourceDeliveries;
    }

    @Override
    public List<SourceSale> extractSourceCsvSale() {
        List<File> saleFiles = utilService.getDirectoryFilesWithPrefix("csv", "Sale_");
        List<SourceSale> sourceSales = new LinkedList<>();
        for(File file : saleFiles) {
            List<Csv_Sale> csvSales = extractSaleCsv(file.getPath());

            for(Csv_Sale csvSale : csvSales) {
                SourceSale newObject = createSale(csvSale);
                sourceSales.add(newObject);
            }
        }
        return sourceSales;
    }

    private List<Csv_Delivery> extractDeliveryCsv(String filePath) {
        List<String> ord = new ArrayList<>();
        ord.add("idDelivery");
        ord.add("nameProvider");
        ord.add("codeProduct");
        ord.add("quantityProduct");
        return new CsvReader<>(Csv_Delivery.class, filePath, true, ";").setOrder(ord).read().getData();
    }

    private List<Csv_Sale> extractSaleCsv(String filePath) {
        List<String> ord = new ArrayList<>();
        ord.add("idSale");
        ord.add("codeProduct");
        ord.add("nameShop");
        ord.add("quantityProduct");
        return new CsvReader<>(Csv_Sale.class, filePath, true, ";").setOrder(ord).read().getData();
    }

    private SourceDelivery createDelivery(Csv_Delivery object) {
        SourceDelivery newObject = new SourceDelivery();
        newObject.setIdDelivery((Long.valueOf(object.getIdDelivery())));
        newObject.setNameProvider(object.getNameProvider());
        newObject.setCodeProduct(object.getCodeProduct());
        newObject.setQuantityProduct(Long.valueOf(object.getQuantityProduct()));
        return newObject;
    }

    private SourceSale createSale(Csv_Sale object) {
        SourceSale newObject = new SourceSale();
        newObject.setIdSale((Long.valueOf(object.getIdSale())));
        newObject.setCodeProduct(object.getCodeProduct());
        newObject.setNameShop(object.getNameShop());
        newObject.setQuantityProduct(Long.valueOf(object.getQuantityProduct()));
        return newObject;
    }

    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    public void setSourceOneBrandExtractor(SourceOneBrandExtractor sourceOneBrandExtractor) {
        this.sourceOneBrandExtractor = sourceOneBrandExtractor;
    }

    public void setSourceOneCountryExtractor(SourceOneCountryExtractor sourceOneCountryExtractor) {
        this.sourceOneCountryExtractor = sourceOneCountryExtractor;
    }

    public void setSourceOneProducerExtractor(SourceOneProducerExtractor sourceOneProducerExtractor) {
        this.sourceOneProducerExtractor = sourceOneProducerExtractor;
    }

    public void setSourceOneProductExtractor(SourceOneProductExtractor sourceOneProductExtractor) {
        this.sourceOneProductExtractor = sourceOneProductExtractor;
    }

    public void setSourceOneProviderExtractor(SourceOneProviderExtractor sourceOneProviderExtractor) {
        this.sourceOneProviderExtractor = sourceOneProviderExtractor;
    }

    public void setSourceOneTypePriceExtractor(SourceOneTypePriceExtractor sourceOneTypePriceExtractor) {
        this.sourceOneTypePriceExtractor = sourceOneTypePriceExtractor;
    }

    public void setSourceTwoLocalityExtractor(SourceTwoLocalityExtractor sourceTwoLocalityExtractor) {
        this.sourceTwoLocalityExtractor = sourceTwoLocalityExtractor;
    }

    public void setSourceTwoRegionExtractor(SourceTwoRegionExtractor sourceTwoRegionExtractor) {
        this.sourceTwoRegionExtractor = sourceTwoRegionExtractor;
    }

    public void setSourceTwoShopExtractor(SourceTwoShopExtractor sourceTwoShopExtractor) {
        this.sourceTwoShopExtractor = sourceTwoShopExtractor;
    }

    public void setSourceTwoTypeWorkerExtractor(SourceTwoTypeWorkerExtractor sourceTwoTypeWorkerExtractor) {
        this.sourceTwoTypeWorkerExtractor = sourceTwoTypeWorkerExtractor;
    }

    public void setSourceTwoWorkerExtractor(SourceTwoWorkerExtractor sourceTwoWorkerExtractor) {
        this.sourceTwoWorkerExtractor = sourceTwoWorkerExtractor;
    }

    public void setSourceDeliveryExtractor(SourceDeliveryExtractor sourceDeliveryExtractor) {
        this.sourceDeliveryExtractor = sourceDeliveryExtractor;
    }

    public void setSourceSaleExtractor(SourceSaleExtractor sourceSaleExtractor) {
        this.sourceSaleExtractor = sourceSaleExtractor;
    }
}
