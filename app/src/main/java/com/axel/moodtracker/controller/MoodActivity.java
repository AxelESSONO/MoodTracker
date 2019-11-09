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
        iconeItemList.add(new IconeItem("smiley_sad", "sad" ,"#AB1A49"));
        iconeItemList.add(new IconeItem("smiley_disappointed", "disappointed" ,"#808A89"));
        iconeItemList.add(new IconeItem("smiley_normal","normal", "#3135D0"));
        iconeItemList.add(new IconeItem("smiley_happy","happy","#55B617"));
        iconeItemList.add(new IconeItem("smiley_super_happy","super_happy","#D0E807"));

        //get listV iew
        ListView pictureListView = findViewById(R.id.list_picture);
        pictureListView.setAdapter(new IconItemAdapter(this, iconeItemList));
    }
}
