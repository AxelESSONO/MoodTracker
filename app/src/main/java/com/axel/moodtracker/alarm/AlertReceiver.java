package com.axel.moodtracker.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.axel.moodtracker.R;
import com.axel.moodtracker.model.Mood;
import com.axel.moodtracker.utils.Constants;
import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class AlertReceiver extends BroadcastReceiver {

    private String recentComment, saveCurrentDate;
    private int moodImageByPosition, colorByPosition, currentItem;
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        getTodayMood(context);
        Mood mood = new Mood(recentComment, colorByPosition, saveCurrentDate, moodImageByPosition, currentItem);
        saveData(context, mood);
        initTmpData(context);
    }

    private void getTodayMood(Context context) {
        preferences = context.getSharedPreferences(Constants.TODAY_MOOD, MODE_PRIVATE);
        recentComment = preferences.getString(Constants.RECENT_COMMENT, "");
        saveCurrentDate = preferences.getString(Constants.SAVE_CURRENT_DATE, null);
        moodImageByPosition = preferences.getInt(Constants.MOOD_IMAGE_BY_POSITION, R.drawable.no_data);
        colorByPosition = preferences.getInt(Constants.COLOR_BY_POSITION, context.getResources().getColor(R.color.colorBackgroundGrey));
        currentItem = preferences.getInt(Constants.CURRENT_ITEM, 3);
    }

    private void saveData(Context context, Mood mood) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.RECENT_MOOD, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mood);
        prefsEditor.putString(Constants.MOOD, json);
        prefsEditor.commit();
        Toasty.success(context, R.string.mood_saved_successfully, Toast.LENGTH_SHORT, true).show();
    }

    private void initTmpData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MOOD_TMP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.COLOR_BY_POSITION, context.getResources().getIntArray(R.array.colorPagesViewPager)[3]);
        editor.putInt(Constants.MOOD_IMAGE_BY_POSITION, R.drawable.d_smiley_happy);
        editor.putInt(Constants.CURRENT_ITEM, 3);
        editor.commit();
    }
}