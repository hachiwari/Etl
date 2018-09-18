package com.tkurek.wat.Etl.model.warehouse;

import java.sql.Timestamp;

public class W_Country {

    private Long id;
    private Long idCountry;
    private String name;
    private String code;
    private Timestamp timestampFrom;
    private Timestamp timestampTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(Long idCountry) {
        this.idCountry = idCountry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
