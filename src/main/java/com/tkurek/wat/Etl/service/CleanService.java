package com.tkurek.wat.Etl.service;

public interface CleanService {

    void cleanAllTables();
    boolean cleanPhase(String phaseName);
}
