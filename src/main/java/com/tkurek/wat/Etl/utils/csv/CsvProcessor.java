package com.tkurek.wat.Etl.utils.csv;

public interface CsvProcessor<T> {
    T process(T inData);
}
