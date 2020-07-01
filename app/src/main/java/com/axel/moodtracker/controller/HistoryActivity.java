package com.axel.moodtracker.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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

import static com.axel.moodtracker.utils.Constants.PREF_DATA;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<Mood> moodArrayList;
    private RecyclerView mRecyclerView;
    private MoodAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        load();

        sizeMoodList(moodArrayList);

        initRecyclerView();
    }

    private void sizeMoodList(ArrayList<Mood> moodArrayList) {

        if (moodArrayList.size() > 7) {
            int i = 0;
            do {
                moodArrayList.remove(i);
                i++;
            } while (moodArrayList.size() > 7);
        }
    }

    private void load() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_DATA, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.MOOD_LIST, null);
        Type type = new TypeToken<ArrayList<Mood>>() {
        }.getType();
        moodArrayList = gson.fromJson(json, type);
        if (moodArrayList == null) {
            moodArrayList = new ArrayList<>();
        }
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_mood);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MoodAdapter(moodArrayList, HistoryActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(HistoryActivity.this, MoodActivity.class));
    }
}