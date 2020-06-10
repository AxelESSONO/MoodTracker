package com.axel.moodtracker.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.axel.moodtracker.R;
import com.axel.moodtracker.adapter.MoodAdapter;
import com.axel.moodtracker.model.Mood;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private static final String PREF_DATA = "PREF_DATA";

    ArrayList<Mood> moodArrayList;
    private RecyclerView mRecyclerView;
    private MoodAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getData();

    }

    private void getData() {
        load();
        initRecyclerView();
    }


    private void load() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_DATA, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("mood list", null);
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
        mAdapter = new MoodAdapter(moodArrayList, getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}