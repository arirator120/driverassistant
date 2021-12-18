package com.example.dthdriverassistant;

import java.io.Serializable;
import java.util.ArrayList;

public class Remind  implements Serializable {
    private  int avatar;
    private String action;
    private String day;
    private String note;

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static ArrayList<Remind> init(){
        String[] actions = {"Rửa xe"};
        String[] days = {"9/12/2021"};
        String[] notes = {"Tiệm anh Thành Q7"};
        int[] images = {R.drawable.ic_time};
        ArrayList<Remind> Reminds = new ArrayList<>();
        for (int i = 0; i < actions.length; i++){
            Remind r = new Remind();
            r.setAvatar(images[i]);
            r.setAction(actions[i]);
            r.setDay(days[i]);
            r.setNote(notes[i]);
            Reminds.add(r);
        }
        return  Reminds;
    }
}
