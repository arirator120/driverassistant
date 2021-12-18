package com.example.dthdriverassistant.model;
import java.io.Serializable;

public class fuel implements Serializable {

    private String calFilled, timeFilled, address, decs, id;
    private int price;
    private double amountFilled, kmFilled;
    private vehicle vehicle;
    private String idUser;

    public fuel() {
    }

    public fuel(String calFilled, String timeFilled, String address, String decs, String id, int price, double amountFilled, double kmFilled, vehicle vehicle, String idUser) {
        this.calFilled = calFilled;
        this.timeFilled = timeFilled;
        this.address = address;
        this.decs = decs;
        this.id = id;
        this.price = price;
        this.amountFilled = amountFilled;
        this.kmFilled = kmFilled;
        this.vehicle = vehicle;
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public double getAmountFilled() {
        return amountFilled;
    }

    public void setAmountFilled(double amountFilled) {
        this.amountFilled = amountFilled;
    }

    public double getKmFilled() {
        return kmFilled;
    }

    public void setKmFilled(double kmFilled) {
        this.kmFilled = kmFilled;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public com.example.dthdriverassistant.model.vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(com.example.dthdriverassistant.model.vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
