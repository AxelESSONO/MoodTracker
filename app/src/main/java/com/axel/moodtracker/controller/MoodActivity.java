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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.axel.moodtracker.R;
import com.axel.moodtracker.ViewPager.VerticalViewPager;
import com.axel.moodtracker.adapter.PageAdapter;
import com.axel.moodtracker.fragment.MoodFragment;
import com.axel.moodtracker.model.Mood;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MoodActivity extends AppCompatActivity implements MoodFragment.PassDataInterface {

    private static final String PREF_DATA_FRAGMENT = "PREF_DATA_FRAGMENT";
    private FloatingActionButton addComment, historyMood;
    public static final String KEY_POSITION = "position";
    public static final String KEY_COLOR = "color";
    public static final String PREF_DATA = "PREF_DATA";
    public static final String IMAGE_RESSOURCE = "IMAGE RESSOURCE";
    public static final String IMAGE_COLOR = "IMAGE COLOR";
    public static final String POSITION = "POSITION";


    public static final String STOCKAGE_INFOS = "data";

    FragmentManager manager;
    FragmentTransaction transaction;

    private ArrayList<Mood> mMoodList;
    private int mColor;
    private int mPosition;
    private int mImage;
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
        configureViewPager();

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
                checkComment(recentComment, mComent);

                Toast.makeText(MoodActivity.this, recentComment, Toast.LENGTH_SHORT).show();

            }
        });
    }

    // Check if comment is empty
    private void checkComment(String recentComment, EditText mComent) {
        if (recentComment.equals("")) {
            mComent.setError(getResources().getString(R.string.no_comment_error));
        } else {
            saveComment(recentComment, mComent);
        }
    }

    // Save comment
    private void saveComment(String recentComment, EditText mComent) {
        retreiveColorMoodDrawable(recentComment, mComent);
        Toast.makeText(this, recentComment, Toast.LENGTH_SHORT).show();
        mComent.setText("");
        mComent.setHint(getResources().getString(R.string.please_write_a_comment_here));
    }

    private void retreiveColorMoodDrawable(String recentComment, EditText mComent) {

        // Get date and time
        final String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        // Get Color
        //mColor = getColor();


        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_DATA_FRAGMENT, MODE_PRIVATE);
        int pColor = pref.getInt("Couleur", -11160041);
        int pPosition = pref.getInt("Position", 3);

        load();
        insertItem(recentComment, mColor, saveCurrentDate);
        saveData();

    }

    private int getColor() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_DATA, MODE_PRIVATE);
        int pColor = pref.getInt(IMAGE_COLOR, -11160041);
        return pColor;
    }

    private void saveMood(String recentComment, int resourceColor, String saveCurrentDate) {

        mMoodList = new ArrayList<>();

        mMoodList.add(new Mood(recentComment, resourceColor, saveCurrentDate));
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mMoodList);
        editor.putString("mood list", json);
        editor.apply();
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

    private void configureViewPager() {

        manager = getSupportFragmentManager();
        // 2 - Set Adapter PageAdapter and glue it together
        PagerAdapter pagerAdapter = new PageAdapter(manager, getResources().getIntArray(R.array.colorPagesViewPager));
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(3); // Setting default MoodFragment
        pager.getCurrentItem();
    }

    @Override
    public void onDataReceived(int color, int position) {

        saveDataPref(color, position);
    }

    private void saveDataPref(int color, int position) {
        mColor = color;
        mPosition = position;
        SharedPreferences datPreferences = getSharedPreferences(PREF_DATA_FRAGMENT, MODE_PRIVATE);
        SharedPreferences.Editor dataEditor = datPreferences.edit();
        dataEditor.putInt("Couleur", mColor);
        dataEditor.putInt("Position", mPosition);
    }
}