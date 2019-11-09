package com.axel.moodtracker.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.axel.moodtracker.R;
import com.axel.moodtracker.adapters.IconItemAdapter;
import com.axel.moodtracker.model.IconeItem;
import java.util.ArrayList;
import java.util.List;

public class MoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        List<IconeItem> iconeItemList = new ArrayList<>();
        iconeItemList.add(new IconeItem("smiley_disappointed", "red"));
        iconeItemList.add(new IconeItem("smiley_sad", "grey"));
        iconeItemList.add(new IconeItem("smiley_normal", "blue"));
        iconeItemList.add(new IconeItem("smiley_happy", "green"));
        iconeItemList.add(new IconeItem("smiley_super_happy", "yellow"));

        //get listView
        ListView pictureListView = findViewById(R.id.list_picture);
        pictureListView.setAdapter(new IconItemAdapter(this, iconeItemList));
    }
}
