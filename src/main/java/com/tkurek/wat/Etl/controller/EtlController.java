package com.tkurek.wat.Etl.controller;

import com.tkurek.wat.Etl.mapper.SourceOneMapper;
import com.tkurek.wat.Etl.mapper.SourceTwoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EtlController {

    private static final Logger LOG = LoggerFactory.getLogger(EtlController.class);

    private SourceOneMapper sourceOneMapper;
    private SourceTwoMapper sourceTwoMapper;

    @Autowired
    private EtlController(SourceOneMapper sourceOneMapper, SourceTwoMapper sourceTwoMapper) {
        this.sourceOneMapper = sourceOneMapper;
        this.sourceTwoMapper = sourceTwoMapper;
    }

    @ResponseBody
    @RequestMapping("/runEtl")
    String runEtl() {
        String ret = this.sourceOneMapper.testSelect(1L) + " " + this.sourceTwoMapper.testSelect2(2L);
        return ret;
    }

}
