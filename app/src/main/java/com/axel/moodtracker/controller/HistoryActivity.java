package com.axel.moodtracker.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axel.moodtracker.R;
import com.axel.moodtracker.adapter.MoodAdapter;
import com.axel.moodtracker.model.Mood;
import com.axel.moodtracker.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<Mood> moodArrayList;
    private RecyclerView mRecyclerView;
    private LinearLayout linearNoDataFound;
    private MoodAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mComment, mDate;
    private int mColor, mImage, mMoodPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        loadData();
        initRecyclerView();
        getMoodArray();
    }

    private void getMoodArray() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.RECENT_MOOD, MODE_PRIVATE);
        /** Get Mood **/
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.MOOD, "");
        Mood mood = gson.fromJson(json, Mood.class);
        /** END **/

        if (json.equals("")) {
            linearNoDataFound.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            linearNoDataFound.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            updateArrayList(mood);
        }
    }

    private void updateArrayList(Mood mood) {
        if (moodArrayList == null) {
            linearNoDataFound.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            insertItem(mood);
            saveUpdateArrayList();
        }
    }

    private void saveUpdateArrayList() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(moodArrayList);
        editor.putString(Constants.MOOD_LIST, json);
        editor.apply();
    }

    private void insertItem(Mood mood) {
        String dateMood = mood.getDate();
        if (moodArrayList.size() > 0) {
            int i = 0;
            do {
                String dateSave = moodArrayList.get(i).getDate();
                if (dateSave.equals(dateMood)){
                    moodArrayList.remove(i);
                }
                i++;
            } while ( i < moodArrayList.size());
        }
        moodArrayList.add(mood);
        mAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_mood);
        linearNoDataFound = (LinearLayout) findViewById(R.id.linear_no_data_found);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MoodAdapter(moodArrayList, HistoryActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent moodActivityIntent = new Intent(HistoryActivity.this, MoodActivity.class);
        moodActivityIntent.putExtra(Constants.COME_FROM_MOOD_ACTIVITY, true);
        startActivity(moodActivityIntent);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_DATA, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.MOOD_LIST, null);
        Type type = new TypeToken<ArrayList<Mood>>() {
        }.getType();
        moodArrayList = gson.fromJson(json, type);
        if (moodArrayList == null) {
            moodArrayList = new ArrayList<>();
        } else {
            sizeMoodList(moodArrayList, 6);
        }
    }

    private void sizeMoodList(ArrayList<Mood> moodArrayList, int countIncrement) {
        if (moodArrayList.size() > countIncrement) {
            int i = 0;
            do {
                moodArrayList.remove(i);
                i++;
            } while (moodArrayList.size() > 7);
        }
    }
}