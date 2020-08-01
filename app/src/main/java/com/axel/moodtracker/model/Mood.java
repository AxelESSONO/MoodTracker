package com.axel.moodtracker.model;

public class Mood {

    private String mComment = null;
    private int mColor;
    private String mDate = null;
    private int mImage;
    private int mMoodPosition;

    public Mood(String mComment, int mColor, String mDate, int mImage, int mMoodPosition) {
        this.mComment = mComment;
        this.mColor = mColor;
        this.mDate = mDate;
        this.mImage = mImage;
        this.mMoodPosition = mMoodPosition;

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

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }

    public int getmMoodPosition() {
        return mMoodPosition;
    }

    public void setmMoodPosition(int mMoodPosition) {
        this.mMoodPosition = mMoodPosition;
    }
}