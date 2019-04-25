package com.uykazdal.gezginapp;

public class CityModel {
    private String name;
    private int photo;
    private String content;

    public CityModel(String name, int photo, String content) {
        this.name = name;
        this.photo = photo;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
