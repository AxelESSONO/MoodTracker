package com.axel.moodtracker.utils;

import com.axel.moodtracker.R;

import java.util.Calendar;

public class Constants {

    public static final String MOOD_LIST = "mood list";
    public static final String MOOD_INTENT = "MOOD_INTENT";
    public static final String PREF_DATA = "PREF_DATA";
    public static final String IMAGE_RESSOURCE = "IMAGE RESSOURCE";
    public static final String IMAGE_COLOR = "IMAGE COLOR";
    public static final String STOCKAGE_INFOS = "data";
    public static final String RECENT_MOOD = "PREF_KEY_COLOR";
    public static final String MOOD_SAVED_YESTERDAY = "MoodSavedYestergay";
    public static final String MOOD_IMAGE_BY_POSITION = "moodImageByPosition";
    public static final String SAVE_CURRENT_DATE = "saveCurrentDate";
    public static final String COLOR_BY_POSITION = "colorByPosition";
    public static final String RECENT_COMMENT = "recentComment";
    public static final String CURRENT_ITEM = "currentItem";
    public static final String MY_COMMENT = "myComment";
    public static final String COLOR_MOOD = "colorMood";
    public static final String IMAGE = "Images";
    public static final String MOOD = "mood";
    private static final String PREF_KEY_IMAGE = "PREF_KEY_IMAGE";
    private static final String PREF_KEY_COLOR = "PREF_KEY_COLOR";
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    public static final int HOUR_SCHEDULE = 0;
    public static final int MINUTE_SCHEDULE = 0;
    public static final long FOUR_SECOND = 4000;

    public static final int RESULT_PICK_CONTACT = 1;
    public static final String PREF_PHONE = "PREF_PHONE";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String DEFAULT_PHONE = "0000";

    public static final int mImageRessource[] = {R.drawable.a_smiley_disappointed, R.drawable.b_smiley_sad, R.drawable.c_smiley_normal, R.drawable.d_smiley_happy, R.drawable.e_smiley_super_happy};

    //Remaining time to reach midnight
    public static long remainingTimeToReachMidnight() {

        // Get date and time
        Calendar now = Calendar.getInstance();
        long hours = now.get(Calendar.HOUR_OF_DAY) * 3600000;
        long minute = now.get(Calendar.MINUTE) * 60000;
        long triggerAfter = 86400000 - (hours + minute);
        return triggerAfter;
    }
}
