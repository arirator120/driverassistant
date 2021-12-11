package com.example.main.LichSu;

public class Event {

    private int Id;
    private String TenCV;
    private String Date;
    private String Place;
    private int vnd;
    private boolean Completed;

    public Event() {
    }

    public Event(int id, String tenCV, String date, String place, int vnd, boolean completed) {
        this.Id = id;
        this.TenCV = tenCV;
        this.Date = date;
        this.Place = place;
        this.vnd = vnd;
        this.Completed = completed;
    }

    public Event(String tenCV, String date, String place, int vnd) {
        this.TenCV = tenCV;
        this.Date = date;
        this.Place = place;
        this.vnd = vnd;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getTenCV() {
        return TenCV;
    }

    public void setTenCV(String tenCV) {
        this.TenCV = tenCV;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        this.Place = place;
    }

    public int getVnd() {
        return vnd;
    }

    public void setVnd(int vnd) {
        this.vnd = vnd;
    }

    public boolean getCompleted() {
        return Completed;
    }

    public void setCompleted(boolean completed) {
        Completed = completed;
    }
}
