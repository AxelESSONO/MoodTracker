package com.axel.moodtracker.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import com.axel.moodtracker.R;
import com.axel.moodtracker.ViewPager.VerticalViewPager;
import com.axel.moodtracker.adapter.PageAdapter;
import com.axel.moodtracker.alarm.AlertReceiver;
import com.axel.moodtracker.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoast.StyleableToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MoodActivity extends AppCompatActivity {

    private FloatingActionButton addComment, historyMood;
    private FragmentManager manager;
    private VerticalViewPager pager;
    private String recentComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mood);

        addComment = (FloatingActionButton) findViewById(R.id.add_comment);
        historyMood = (FloatingActionButton) findViewById(R.id.go_to_history);

        // 1 - Get ViewPager from layout
        pager = (VerticalViewPager) findViewById(R.id.activity_main_viewpager);

        manager = getSupportFragmentManager();
        // 2 - Set Adapter PageAdapter and glue it together
        PagerAdapter pagerAdapter = new PageAdapter(manager, getResources().getIntArray(R.array.colorPagesViewPager));
        configureViewPager(pagerAdapter);

        // To display popup where the the comment will be written
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        historyMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(MoodActivity.this, HistoryActivity.class);
                startActivity(historyIntent);
            }
        });
    }

    // Display popup
    private void showPopup() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MoodActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.comment_popup_layout, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();  // Dialog popup is showing
        TextView mTitle = (TextView) mView.findViewById(R.id.title);
        Button mValidateBtn = (Button) mView.findViewById(R.id.validate);
        Button mCancelBtn = (Button) mView.findViewById(R.id.cancel);

        // When user cancel to write comment
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // When User validate comment
        mValidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mComent = (EditText) mView.findViewById(R.id.commentEditText);
                recentComment = mComent.getText().toString();
                checkComment(recentComment, mComent, pager.getCurrentItem());
            }
        });
    }

    // Check if comment is empty
    private void checkComment(String recentComment, EditText mComent, int currentItem) {
        if (recentComment.equals("")) {
            mComent.setError(getResources().getString(R.string.no_comment_error));
        } else {
            saveComment(recentComment, mComent, currentItem);
        }
    }

    // Save comment
    private void saveComment(String recentComment, EditText mComent, int currentItem) {
        retreiveColorMoodDrawable(recentComment, currentItem);
        mComent.setText("");
        mComent.setHint(getResources().getString(R.string.please_write_a_comment_here));
    }

    private void retreiveColorMoodDrawable(String recentComment, int currentItem) {
        // Get date and time
        final String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        setTime(recentComment,
                getColorByPosition(currentItem),
                saveCurrentDate,
                getMoodImageByPosition(currentItem),
                currentItem);
    }

    private int getColorByPosition(int currentItem) {
        int color = getResources().getIntArray(R.array.colorPagesViewPager)[currentItem];
        return color;
    }

    private int getMoodImageByPosition(int currentItem) {
        int image = Constants.mImageRessource[currentItem];
        return image;
    }

    // save mood here
    private void configureViewPager(PagerAdapter pagerAdapter) {
        pager.setAdapter(pagerAdapter);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.RECENT_MOOD, MODE_PRIVATE);
        int itemCurrent = sharedPreferences.getInt(Constants.CURRENT_ITEM, 3);
        pager.setCurrentItem(itemCurrent); // Setting default MoodFragment
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setTime(String recentComment, int colorByPosition, String saveCurrentDate, int moodImageByPosition, int currentItem) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        startAlarm(calendar, recentComment, colorByPosition, saveCurrentDate, moodImageByPosition, currentItem);
    }

    private void startAlarm(Calendar calendar, String recentComment, int colorByPosition, String saveCurrentDate, int moodImageByPosition, int currentItem) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RECENT_COMMENT, recentComment);
        bundle.putInt(Constants.COLOR_BY_POSITION, colorByPosition);
        bundle.putString(Constants.SAVE_CURRENT_DATE, saveCurrentDate);
        bundle.putInt(Constants.MOOD_IMAGE_BY_POSITION, moodImageByPosition);
        bundle.putInt(Constants.CURRENT_ITEM, currentItem);
        intent.putExtras(bundle);

        PendingIntent sender = PendingIntent.getBroadcast(this, 2, intent, 0);
        alarmManager.cancel(sender);
        if (alarmManager != null) {
            if (calendar.after(Calendar.getInstance())) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            }
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }
        }
        displayMessage(getResources().getString(R.string.mood_saved_tomorrow));
    }

    private void displayMessage(String message) {
        new StyleableToast
                .Builder(this)
                .text(message)
                .textColor(Color.WHITE)
                .backgroundColor(Color.GREEN)
                .show();
    }
}