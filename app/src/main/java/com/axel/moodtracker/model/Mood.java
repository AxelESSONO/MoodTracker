package com.axel.moodtracker.model;

public class Mood {

    private String mComment = null;
    private int mColor;
    private String mDate = null;

    public Mood(String mComment, int mColor, String mDate) {
        this.mComment = mComment;
        this.mColor = mColor;
        this.mDate = mDate;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }


}