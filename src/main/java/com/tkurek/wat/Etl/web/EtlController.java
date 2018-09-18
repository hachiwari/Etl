package com.tkurek.wat.Etl.web;

import com.tkurek.wat.Etl.service.ExtractService;
import com.tkurek.wat.Etl.service.TransformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EtlController {

    private static final Logger LOG = LoggerFactory.getLogger(EtlController.class);

    private ExtractService extractService;
    private TransformService transformService;

    @Autowired
    private EtlController(ExtractService extractService, TransformService transformService) {
        this.extractService = extractService;
        this.transformService = transformService;
    }

    @ResponseBody
    @RequestMapping("/runEtl")
    String runEtl() {
        this.extractService.extract();
        this.transformService.transform();
        //TODO load
        LOG.info("Success!");
        return "Success!";
    }

}
