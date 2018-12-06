package com.tkurek.wat.Etl.web;

import com.tkurek.wat.Etl.model.sourceCsv.SourceDelivery;
import com.tkurek.wat.Etl.model.sourceCsv.SourceSale;
import com.tkurek.wat.Etl.model.warehouse.F_Delivery;
import com.tkurek.wat.Etl.model.warehouse.F_Sale;
import com.tkurek.wat.Etl.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Collection;
import java.util.List;

@RestController
public class EtlController {

    private static final Logger LOG = LoggerFactory.getLogger(EtlController.class);

    private ExtractService extractService;
    private TransformService transformService;
    private LoadService loadService;
    private CleanService cleanService;
    private UtilService utilService;

    @Autowired
    private EtlController(ExtractService extractService, TransformService transformService, LoadService loadService, CleanService cleanService, UtilService utilService) {
        this.extractService = extractService;
        this.transformService = transformService;
        this.loadService = loadService;
        this.cleanService = cleanService;
        this.utilService = utilService;
    }

    @ResponseBody
    @RequestMapping("/runEtl")
    String runEtl() {
        extractService.extract();
        transformService.transform();
        loadService.load();
        LOG.info("Success!");
        return "Success!";
    }

    @ResponseBody
    @RequestMapping("/report/delivery")
    Collection<F_Delivery> showAllDeliveries() {
        return loadService.getAllDeliveries();
    }

    @ResponseBody
    @RequestMapping("/report/sale")
    Collection<F_Sale> showAllSales() {
        return loadService.getAllSales();
    }

    @ResponseBody
    @RequestMapping("/cleanAll")
    String cleanAllPhases() {
        cleanService.cleanAllPhases();
        LOG.info("Cleaned all phases!");
        return "Cleaned all phases!";
    }

    @ResponseBody
    @RequestMapping("/cleanPhase/{phaseName}")
    String cleanPhase(@PathVariable("phaseName") String phaseName) {
        if (cleanService.cleanPhase(phaseName)) {
            LOG.info(String.format("Cleaned phase - %s!", phaseName));
            return String.format("Cleaned phase - %s!", phaseName);
        } else {
            LOG.info(String.format("ERROR in cleaned phase - %s!", phaseName));
            return String.format("ERROR in cleaned phase - %s!", phaseName);
        }
    }

    @ResponseBody
    @RequestMapping("/showFiles")
    File[] showFiles() {
        return utilService.getDirectoryFiles("csv");
    }

    @ResponseBody
    @RequestMapping("/showFiles/delivery")
    List<SourceDelivery> showDeliveryFiles() {
        return extractService.extractSourceCsvDelivery();
    }

    @ResponseBody
    @RequestMapping("/showFiles/sale")
    List<SourceSale> showSaleFiles() {
        return extractService.extractSourceCsvSale();
    }
}
