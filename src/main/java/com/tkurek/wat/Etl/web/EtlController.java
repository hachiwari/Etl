package com.tkurek.wat.Etl.web;

import com.tkurek.wat.Etl.service.CleanService;
import com.tkurek.wat.Etl.service.ExtractService;
import com.tkurek.wat.Etl.service.LoadService;
import com.tkurek.wat.Etl.service.TransformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EtlController {

    private static final Logger LOG = LoggerFactory.getLogger(EtlController.class);

    private ExtractService extractService;
    private TransformService transformService;
    private LoadService loadService;
    private CleanService cleanService;

    @Autowired
    private EtlController(ExtractService extractService, TransformService transformService, LoadService loadService, CleanService cleanService) {
        this.extractService = extractService;
        this.transformService = transformService;
        this.loadService = loadService;
        this.cleanService = cleanService;
    }

    @ResponseBody
    @RequestMapping("/runEtl")
    String runEtl() {
        this.extractService.extract();
        this.transformService.transform();
        this.loadService.load();
        LOG.info("Success!");
        return "Success!";
    }

    @ResponseBody
    @RequestMapping("/cleanAll")
    String cleanAllTables() {
        this.cleanService.cleanAllTables();
        LOG.info("Cleaned all tables!");
        return "Cleaned all tables!";
    }

    @ResponseBody
    @RequestMapping("/cleanPhase/{phaseName}")
    String cleanPhase(@PathVariable("phaseName") String phaseName) {
        if (this.cleanService.cleanPhase(phaseName)) {
            LOG.info(String.format("Cleaned phase - %s!", phaseName));
            return String.format("Cleaned phase - %s!", phaseName);
        } else {
            LOG.info(String.format("ERROR in cleaned phase - %s!", phaseName));
            return String.format("ERROR in cleaned phase - %s!", phaseName);
        }
    }
}
