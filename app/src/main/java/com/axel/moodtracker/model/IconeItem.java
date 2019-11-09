package com.axel.moodtracker.model;

public class IconeItem {
    // field
    private String name;
    private String color;
    private String mnemonic;

    // constructor
    public IconeItem(String name,String mnemonic, String color){
        this.name = name;
        this.color = color;
        this.mnemonic = mnemonic;
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

    public String getMnemonic() {
        return mnemonic;
    }
}
