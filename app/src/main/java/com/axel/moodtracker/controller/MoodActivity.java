package com.axel.moodtracker.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import com.axel.moodtracker.R;
import com.axel.moodtracker.ViewPager.VerticalViewPager;
import com.axel.moodtracker.adapter.PageAdapter;
import com.axel.moodtracker.model.Mood;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MoodActivity extends AppCompatActivity {

    private FloatingActionButton addComment, historyMood;
    public static final String PREF_DATA = "PREF_DATA";
    public static final String IMAGE_RESSOURCE = "IMAGE RESSOURCE";
    public static final String IMAGE_COLOR = "IMAGE COLOR";
    public static final String STOCKAGE_INFOS = "data";
    FragmentManager manager;
    private ArrayList<Mood> mMoodList;
    VerticalViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mood);

        addComment = (FloatingActionButton) findViewById(R.id.add_comment);
        historyMood = (FloatingActionButton) findViewById(R.id.go_to_history);

        // 1 - Get ViewPager from layout
        pager = (VerticalViewPager) findViewById(R.id.activity_main_viewpager);
        //3 - Configure ViewPager

        manager = getSupportFragmentManager();
        // 2 - Set Adapter PageAdapter and glue it together
        PagerAdapter pagerAdapter = new PageAdapter(manager, getResources().getIntArray(R.array.colorPagesViewPager));
        configureViewPager(manager, pagerAdapter);

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
                String recentComment = mComent.getText().toString();
                checkComment(recentComment, mComent, pager.getCurrentItem());
                Toast.makeText(MoodActivity.this, "La page numero: " + pager.getCurrentItem(), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, recentComment, Toast.LENGTH_SHORT).show();
        mComent.setText("");
        mComent.setHint(getResources().getString(R.string.please_write_a_comment_here));
    }

    private void retreiveColorMoodDrawable(String recentComment, int currentItem) {

        // Get date and time
        final String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        load();
        insertItem(recentComment, getColorByPosition(currentItem), saveCurrentDate);
        saveData();
    }

    private int getColorByPosition(int currentItem) {
        int color = getResources().getIntArray(R.array.colorPagesViewPager)[currentItem];
        return color;
    }

    private void load() {

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_DATA, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("mood list", null);
        Type type = new TypeToken<ArrayList<Mood>>() {
        }.getType();
        mMoodList = gson.fromJson(json, type);
        if (mMoodList == null) {
            mMoodList = new ArrayList<>();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mMoodList);
        editor.putString("mood list", json);
        editor.apply();
    }

    private void insertItem(String mComment, int color, String mDate) {
        mMoodList.add(new Mood(mComment, color, mDate));
    }

    private void configureViewPager(FragmentManager manager, PagerAdapter pagerAdapter) {
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(3); // Setting default MoodFragment
    }
}