package com.tkurek.wat.Etl.web;

import com.tkurek.wat.Etl.service.ExtractService;
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

    @Autowired
    private EtlController(ExtractService extractService) {
        this.extractService = extractService;
    }

    @ResponseBody
    @RequestMapping("/runEtl")
    String runEtl() {
        this.extractService.extractSources();
        //TODO transform and load
        LOG.info("Success!");
        return "Success!";
    }

}
