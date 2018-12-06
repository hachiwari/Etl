package com.tkurek.wat.Etl.service;

public interface CleanService {

    void cleanAllPhases();
    boolean cleanPhase(String phaseName);
}
