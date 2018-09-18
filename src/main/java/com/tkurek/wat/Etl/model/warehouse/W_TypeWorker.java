package com.tkurek.wat.Etl.model.warehouse;

import java.sql.Timestamp;

public class W_TypeWorker {

    private Long id;
    private Long idTypeWorker;
    private String name;
    private Timestamp timestampFrom;
    private Timestamp timestampTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTypeWorker() {
        return idTypeWorker;
    }

    public void setIdTypeWorker(Long idTypeWorker) {
        this.idTypeWorker = idTypeWorker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTimestampFrom() {
        return timestampFrom;
    }

    public void setTimestampFrom(Timestamp timestampFrom) {
        this.timestampFrom = timestampFrom;
    }

    public Timestamp getTimestampTo() {
        return timestampTo;
    }

    public void setTimestampTo(Timestamp timestampTo) {
        this.timestampTo = timestampTo;
    }
}
