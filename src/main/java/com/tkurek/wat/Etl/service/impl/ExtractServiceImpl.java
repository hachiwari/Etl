package com.tkurek.wat.Etl.service.impl;

import com.tkurek.wat.Etl.service.ExtractService;
import com.tkurek.wat.Etl.utils.extractor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractServiceImpl implements ExtractService {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractServiceImpl.class);

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

    @Override
    public void extractSources() {
        LOG.info("Init Extract!");
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
        LOG.info("Done Extract!");
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
}
