package com.tkurek.wat.Etl.model.stage.sourceOne;

import java.sql.Timestamp;

public class Stage_TypePrice {

    private Long id;
    private Long idTypePrice;
    private String name;
    private Timestamp timestampFrom;
    private Timestamp timestampTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTypePrice() {
        return idTypePrice;
    }

    public void setIdTypePrice(Long idTypePrice) {
        this.idTypePrice = idTypePrice;
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
