package com.example.dthdriverassistant.model;

import java.io.Serializable;

public class remind implements Serializable {
    private String tvAction, tvDay, tvNote, id;
    private vehicle vehicle;
    private String idUser;

    public  remind(){

    }

    public remind(String tvAction, String tvDay, String tvNote, String id, com.example.dthdriverassistant.model.vehicle vehicle, String idUser) {
        this.tvAction = tvAction;
        this.tvDay = tvDay;
        this.tvNote = tvNote;
        this.id = id;
        this.vehicle = vehicle;
        this.idUser = idUser;
    }

    public String getTvAction() {
        return tvAction;
    }

    public void setTvAction(String tvAction) {
        this.tvAction = tvAction;
    }

    public String getTvDay() {
        return tvDay;
    }

    public void setTvDay(String tvDay) {
        this.tvDay = tvDay;
    }

    public String getTvNote() {
        return tvNote;
    }

    public void setTvNote(String tvNote) {
        this.tvNote = tvNote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public com.example.dthdriverassistant.model.vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(com.example.dthdriverassistant.model.vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
