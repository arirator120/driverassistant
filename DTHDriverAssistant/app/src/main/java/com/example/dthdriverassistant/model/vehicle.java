package com.example.dthdriverassistant.model;

import java.io.Serializable;

public class vehicle implements Serializable {
    private String id, name, sign, company, dateBuy, desc, idUser;
    private double currKm;
    private type type;

    public vehicle() {

    }

    public vehicle(String id, String name, String sign, type type, String company, String dateBuy, double currKm, String desc, String idUser) {
        this.id = id;
        this.name = name;
        this.sign = sign;
        this.type = type;
        this.company = company;
        this.dateBuy = dateBuy;
        this.currKm = currKm;
        this.desc = desc;
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public type getType() {
        return type;
    }

    public void setType(type type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDateBuy() {
        return dateBuy;
    }

    public void setDateBuy(String dateBuy) {
        this.dateBuy = dateBuy;
    }

    public double getCurrKm() {
        return currKm;
    }

    public void setCurrKm(double currKm) {
        this.currKm = currKm;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    @Override
    public String toString() {
        return getName(); // You can add anything else like maybe getDrinkType()
    }
}
