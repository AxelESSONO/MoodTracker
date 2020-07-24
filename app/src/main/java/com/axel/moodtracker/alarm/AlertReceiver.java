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
import static android.content.Context.MODE_PRIVATE;

public class AlertReceiver extends BroadcastReceiver {

    private String recentComment, saveCurrentDate;
    private int moodImageByPosition, colorByPosition, currentItem;

    @Override
    public void onReceive(Context context, Intent intent) {

        recentComment = intent.getStringExtra(Constants.RECENT_COMMENT);
        saveCurrentDate = intent.getStringExtra(Constants.SAVE_CURRENT_DATE);
        moodImageByPosition = intent.getIntExtra(Constants.MOOD_IMAGE_BY_POSITION, R.drawable.no_data);
        colorByPosition = intent.getIntExtra(Constants.COLOR_BY_POSITION, context.getResources().getColor(R.color.colorBackgroundGrey));
        currentItem = intent.getIntExtra(Constants.CURRENT_ITEM, 3);
        Mood mood = new Mood(recentComment, colorByPosition, saveCurrentDate, moodImageByPosition, currentItem);
        saveData(context, mood);
    }

    private void saveData(Context context, Mood mood) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.RECENT_MOOD, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mood);
        prefsEditor.putString(Constants.MOOD, json);
        prefsEditor.commit();
        Toast.makeText(context, mood.getComment(), Toast.LENGTH_SHORT).show();
    }
}