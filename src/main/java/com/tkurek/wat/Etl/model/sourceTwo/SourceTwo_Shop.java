package com.tkurek.wat.Etl.model.sourceTwo;

public class SourceTwo_Shop {

    private Long idShop;
    private Long idLocality;
    private String name;
    private String phone;
    private String address;
    private String zipCode;

    public Long getIdShop() {
        return idShop;
    }

    public void setIdShop(Long idShop) {
        this.idShop = idShop;
    }

    public Long getIdLocality() {
        return idLocality;
    }

    public void setIdLocality(Long idLocality) {
        this.idLocality = idLocality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
