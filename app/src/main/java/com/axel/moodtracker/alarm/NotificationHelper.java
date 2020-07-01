package com.axel.moodtracker.alarm;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.axel.moodtracker.R;
import com.axel.moodtracker.model.Mood;
import com.axel.moodtracker.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import static com.axel.moodtracker.utils.Constants.channelID;
import static com.axel.moodtracker.utils.Constants.channelName;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager mManager;
    private ArrayList<Mood> mMoodList;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {

        String notificationMessage = null;

        // Save Mood
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.RECENT_MOOD, MODE_PRIVATE);
        String recentComment = sharedPreferences.getString(Constants.RECENT_COMMENT, null);
        int colorByPosition = sharedPreferences.getInt(Constants.COLOR_BY_POSITION, getResources().getIntArray(R.array.colorPagesViewPager)[3]);
        String saveCurrentDate = sharedPreferences.getString(Constants.SAVE_CURRENT_DATE, null);
        int moodImageByPosition = sharedPreferences.getInt(Constants.MOOD_IMAGE_BY_POSITION, R.drawable.d_smiley_happy);
        boolean isMoodSavedYesterday = sharedPreferences.getBoolean(Constants.MOOD_SAVED_YESTERDAY, false);
        int moodPosition = sharedPreferences.getInt(Constants.CURRENT_ITEM, 3);

        notificationMessage = initData(recentComment,colorByPosition, saveCurrentDate, moodImageByPosition, moodPosition, isMoodSavedYesterday, sharedPreferences);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(notificationMessage)
                .setSmallIcon(R.drawable.icone_moodtracker);
    }

    private String initData(String recentComment,
                            int colorByPosition,
                            String saveCurrentDate,
                            int moodImageByPosition,
                            int moodPosition,
                            boolean isMoodSavedYesterday,
                            SharedPreferences sharedPreferences) {
        String message;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.CURRENT_ITEM, 3);

        if (isMoodSavedYesterday) {
            load();
            insertItem(recentComment, colorByPosition, saveCurrentDate, moodImageByPosition, moodPosition);
            saveData();
            message = getResources().getString(R.string.mood_saved_yesterday);
            editor.putBoolean(Constants.MOOD_SAVED_YESTERDAY, false);
            editor.apply();

        } else {
            message = getResources().getString(R.string.no_mood_saved_yesterday);
            editor.putBoolean(Constants.MOOD_SAVED_YESTERDAY, false);
            editor.apply();
        }
        return message;
    }

    private void load() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_DATA, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.MOOD_LIST, null);
        Type type = new TypeToken<ArrayList<Mood>>() {
        }.getType();
        mMoodList = gson.fromJson(json, type);
        if (mMoodList == null) {
            mMoodList = new ArrayList<>();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mMoodList);
        editor.putString(Constants.MOOD_LIST, json);
        editor.apply();
    }

    private void insertItem(String mComment, int color, String mDate, int mImage, int mMoodPosition) {
        mMoodList.add(new Mood(mComment, color, mDate, mImage, mMoodPosition));
    }
}