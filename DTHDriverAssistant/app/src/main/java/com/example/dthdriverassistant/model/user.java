package com.example.dthdriverassistant.model;

import java.io.Serializable;

public class user implements Serializable {
    private String id, email, avatar, name, phone;

    public user() {
    }

    public user(String id, String email, String avatar, String name, String phone) {
        this.id = id;
        this.email = email;
        this.avatar = avatar;
        this.name = name;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
}
