package com.tkurek.wat.Etl.model.warehouse;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class F_Sale {

    private Long id;
    private Long idSale;
    private Long idProduct;
    private Long idShop;
    private Long quantityProduct;
    private BigDecimal price;
    private Long idTypePrice;
    private Timestamp timestampFrom;
    private Timestamp timestampTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSale() {
        return idSale;
    }

    public void setIdSale(Long idSale) {
        this.idSale = idSale;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public Long getIdShop() {
        return idShop;
    }

    public void setIdShop(Long idShop) {
        this.idShop = idShop;
    }

    public Long getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(Long quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getIdTypePrice() {
        return idTypePrice;
    }

    public void setIdTypePrice(Long idTypePrice) {
        this.idTypePrice = idTypePrice;
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
