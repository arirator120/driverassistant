package com.example.dthdriverassistant.model;

import java.io.Serializable;

public class part implements Serializable {
    private String calFilled, timeFilled, decs, id, namePart, idUser;
    private int price, amount;
    private vehicle vehicle;

    public part() {
    }

    public part(String calFilled, String timeFilled, String decs, String id, String namePart, String idUser, int price, int amount,vehicle vehicle) {
        this.calFilled = calFilled;
        this.timeFilled = timeFilled;
        this.decs = decs;
        this.id = id;
        this.namePart = namePart;
        this.idUser = idUser;
        this.price = price;
        this.amount = amount;
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

    public String getNamePart() {
        return namePart;
    }

    public void setNamePart(String namePart) {
        this.namePart = namePart;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
