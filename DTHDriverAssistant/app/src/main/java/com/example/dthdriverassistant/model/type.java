package com.example.dthdriverassistant.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class type implements Serializable {
    private String id, name;

    public type() {
    }

    public type(String id, String name) {
        this.id = id;
        this.name = name;
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

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
