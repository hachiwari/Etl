package com.tkurek.wat.Etl.service;

public interface UtilService {

    void setSourceToStageId(Long idSource, Long idStage, Class classSource, Class classStage);
}
