package com.example.dthdriverassistant.model;

import java.io.Serializable;

public class oil  implements Serializable {
    private String calFilled, timeFilled, decs, id, type, idUser;
    private int price;
    private double kmChangedOil;
    private vehicle vehicle;

    public oil() {
    }

    public oil(String calFilled, String timeFilled, String decs, String id, String type, String idUser, int price, double kmChangedOil, vehicle vehicle) {
        this.calFilled = calFilled;
        this.timeFilled = timeFilled;
        this.decs = decs;
        this.id = id;
        this.type = type;
        this.idUser = idUser;
        this.price = price;
        this.kmChangedOil = kmChangedOil;
        this.vehicle = vehicle;
    }

    public String getCalFilled() {
        return calFilled;
    }

    public void setCalFilled(String calFilled) {
        this.calFilled = calFilled;
    }

    public String getTimeFilled() {
        return timeFilled;
    }

    public void setTimeFilled(String timeFilled) {
        this.timeFilled = timeFilled;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getKmChangedOil() {
        return kmChangedOil;
    }

    public void setKmChangedOil(double kmChangedOil) {
        this.kmChangedOil = kmChangedOil;
    }

    public vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
