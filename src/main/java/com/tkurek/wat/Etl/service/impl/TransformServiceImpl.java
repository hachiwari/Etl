package com.tkurek.wat.Etl.service.impl;

import com.tkurek.wat.Etl.service.TransformService;
import com.tkurek.wat.Etl.utils.transformer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformServiceImpl implements TransformService {

    private static final Logger LOG = LoggerFactory.getLogger(TransformServiceImpl.class);

    private BrandTransformer brandTransformer;
    private CountryTransformer countryTransformer;
    private ProducerTransformer producerTransformer;
    private ProviderTransformer providerTransformer;
    private TypePriceTransformer typePriceTransformer;
    private ProductTransformer productTransformer;
    private LocalityTransformer localityTransformer;
    private RegionTransformer regionTransformer;
    private ShopTransformer shopTransformer;
    private TypeWorkerTransformer typeWorkerTransformer;
    private WorkerTransformer workerTransformer;
    private DeliveryTransformer deliveryTransformer;
    private SaleTransformer saleTransformer;

    @Override
    public void transform() {
        LOG.info("Init Transform");
        this.brandTransformer.transform();
        this.countryTransformer.transform();
        this.producerTransformer.transform();
        this.providerTransformer.transform();
        this.typePriceTransformer.transform();
        this.productTransformer.transform();
        this.localityTransformer.transform();
        this.regionTransformer.transform();
        this.shopTransformer.transform();
        this.typeWorkerTransformer.transform();
        this.workerTransformer.transform();
        this.deliveryTransformer.transform();
        this.saleTransformer.transform();
        LOG.info("Done Transform");
    }

    public void setBrandTransformer(BrandTransformer brandTransformer) {
        this.brandTransformer = brandTransformer;
    }

    public void setCountryTransformer(CountryTransformer countryTransformer) {
        this.countryTransformer = countryTransformer;
    }

    public void setProducerTransformer(ProducerTransformer producerTransformer) {
        this.producerTransformer = producerTransformer;
    }

    public void setProviderTransformer(ProviderTransformer providerTransformer) {
        this.providerTransformer = providerTransformer;
    }

    public void setTypePriceTransformer(TypePriceTransformer typePriceTransformer) {
        this.typePriceTransformer = typePriceTransformer;
    }

    public void setProductTransformer(ProductTransformer productTransformer) {
        this.productTransformer = productTransformer;
    }

    public void setLocalityTransformer(LocalityTransformer localityTransformer) {
        this.localityTransformer = localityTransformer;
    }

    public void setRegionTransformer(RegionTransformer regionTransformer) {
        this.regionTransformer = regionTransformer;
    }

    public void setShopTransformer(ShopTransformer shopTransformer) {
        this.shopTransformer = shopTransformer;
    }

    public void setTypeWorkerTransformer(TypeWorkerTransformer typeWorkerTransformer) {
        this.typeWorkerTransformer = typeWorkerTransformer;
    }

    public void setWorkerTransformer(WorkerTransformer workerTransformer) {
        this.workerTransformer = workerTransformer;
    }

    public void setDeliveryTransformer(DeliveryTransformer deliveryTransformer) {
        this.deliveryTransformer = deliveryTransformer;
    }

    public void setSaleTransformer(SaleTransformer saleTransformer) {
        this.saleTransformer = saleTransformer;
    }
}
