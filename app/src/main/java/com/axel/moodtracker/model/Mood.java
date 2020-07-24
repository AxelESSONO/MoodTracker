package com.axel.moodtracker.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mood implements Parcelable {

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

    protected Mood(Parcel in) {
        mComment = in.readString();
        mColor = in.readInt();
        mDate = in.readString();
        mImage = in.readInt();
        mMoodPosition = in.readInt();
    }

    public static final Creator<Mood> CREATOR = new Creator<Mood>() {
        @Override
        public Mood createFromParcel(Parcel in) {
            return new Mood(in);
        }

        @Override
        public Mood[] newArray(int size) {
            return new Mood[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mComment);
        dest.writeInt(mColor);
        dest.writeString(mDate);
        dest.writeInt(mImage);
        dest.writeInt(mMoodPosition);
    }
}