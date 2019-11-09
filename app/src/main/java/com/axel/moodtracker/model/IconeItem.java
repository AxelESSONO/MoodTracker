package com.axel.moodtracker.model;

public class IconeItem {
    // field
    private String name;
    private String colorSmiley;
    private String mnemonic;

    // constructor
    public IconeItem(String name,String mnemonic, String colorSmiley){
        this.name = name;
        this.colorSmiley = colorSmiley;
        this.mnemonic = mnemonic;
    }

    // methods
    public String getColorSmiley() {
        return colorSmiley;
    }

    public void setColorSmiley(String colorSmiley) {
        this.colorSmiley = colorSmiley;
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
