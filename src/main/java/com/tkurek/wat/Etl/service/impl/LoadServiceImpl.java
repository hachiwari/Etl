package com.tkurek.wat.Etl.service.impl;

import com.tkurek.wat.Etl.model.warehouse.F_Delivery;
import com.tkurek.wat.Etl.model.warehouse.F_Sale;
import com.tkurek.wat.Etl.service.LoadService;
import com.tkurek.wat.Etl.utils.load.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class LoadServiceImpl implements LoadService {

    private static final Logger LOG = LoggerFactory.getLogger(LoadServiceImpl.class);

    private BrandLoader brandLoader;
    private CountryLoader countryLoader;
    private ProducerLoader producerLoader;
    private ProviderLoader providerLoader;
    private TypePriceLoader typePriceLoader;
    private ProductLoader productLoader;
    private LocalityLoader localityLoader;
    private RegionLoader regionLoader;
    private ShopLoader shopLoader;
    private TypeWorkerLoader typeWorkerLoader;
    private WorkerLoader workerLoader;
    private DeliveryLoader deliveryLoader;
    private SaleLoader saleLoader;

    @Override
    public void load() {
        LOG.info("Init Load");
        this.brandLoader.load();
        this.countryLoader.load();
        this.producerLoader.load();
        this.providerLoader.load();
        this.typePriceLoader.load();
        this.productLoader.load();
        this.localityLoader.load();
        this.regionLoader.load();
        this.shopLoader.load();
        this.typeWorkerLoader.load();
        this.workerLoader.load();
        this.deliveryLoader.load();
        this.saleLoader.load();
        LOG.info("Done Load");
    }

    @Override
    public Collection<F_Delivery> getAllDeliveries() {
        return deliveryLoader.getAllDeliveries();
    }

    @Override
    public Collection<F_Sale> getAllSales() {
        return saleLoader.getAllSales();
    }

    public void setBrandLoader(BrandLoader brandLoader) {
        this.brandLoader = brandLoader;
    }

    public void setCountryLoader(CountryLoader countryLoader) {
        this.countryLoader = countryLoader;
    }

    public void setProducerLoader(ProducerLoader producerLoader) {
        this.producerLoader = producerLoader;
    }

    public void setProviderLoader(ProviderLoader providerLoader) {
        this.providerLoader = providerLoader;
    }

    public void setTypePriceLoader(TypePriceLoader typePriceLoader) {
        this.typePriceLoader = typePriceLoader;
    }

    public void setProductLoader(ProductLoader productLoader) {
        this.productLoader = productLoader;
    }

    public void setLocalityLoader(LocalityLoader localityLoader) {
        this.localityLoader = localityLoader;
    }

    public void setRegionLoader(RegionLoader regionLoader) {
        this.regionLoader = regionLoader;
    }

    public void setShopLoader(ShopLoader shopLoader) {
        this.shopLoader = shopLoader;
    }

    public void setTypeWorkerLoader(TypeWorkerLoader typeWorkerLoader) {
        this.typeWorkerLoader = typeWorkerLoader;
    }

    public void setWorkerLoader(WorkerLoader workerLoader) {
        this.workerLoader = workerLoader;
    }

    public void setDeliveryLoader(DeliveryLoader deliveryLoader) {
        this.deliveryLoader = deliveryLoader;
    }

    public void setSaleLoader(SaleLoader saleLoader) {
        this.saleLoader = saleLoader;
    }
}
