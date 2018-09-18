package com.tkurek.wat.Etl.utils.csv;

public interface CsvProcessor<T> {
    public T process(T inData);
}
