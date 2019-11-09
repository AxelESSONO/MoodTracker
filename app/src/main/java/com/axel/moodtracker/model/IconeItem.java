package com.axel.moodtracker.model;

public class IconeItem {
    // field
    private String name;
    private String color;

    // constructor
    public IconeItem(String name, String color){
        this.name = name;
        this.color = color;
    }

    // methods
    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
