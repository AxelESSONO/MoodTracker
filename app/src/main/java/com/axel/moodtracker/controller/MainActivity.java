package com.axel.moodtracker.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.axel.moodtracker.R;
import com.axel.moodtracker.model.Mood;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_KEY_IMAGE = "PREF_KEY_IMAGE";
    private static final String PREF_KEY_COLOR = "PREF_KEY_COLOR";

    private Mood mood;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private TextView txtAppName;
    private TextView txtVersion;
    private ProgressBar progressBar;
    private Intent mainIntent;

    private String main_comment, main_color, main_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainIntent = new Intent(MainActivity.this, MoodActivity.class);

        imageView = (ImageView) findViewById(R.id.smiley_icon);
        txtAppName = (TextView) findViewById(R.id.name_of_app);
        txtVersion = (TextView) findViewById(R.id.version_name);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_main);

        SharedPreferences data = getApplicationContext().getSharedPreferences(MoodActivity.STOCKAGE_INFOS, MODE_PRIVATE);
        int image = data.getInt(MoodActivity.IMAGE_RESSOURCE, R.drawable.d_smiley_happy);
        String resourceColor = data.getString(MoodActivity.IMAGE_COLOR, "#55B617");

        addDataInDatabase();

        imageView.setImageResource(image);
        linearLayout.setBackgroundColor(Color.parseColor(resourceColor));

        Animation transitionAlfa = AnimationUtils.loadAnimation(this, R.anim.transition_alfa);
        imageView.startAnimation(transitionAlfa);
        txtAppName.startAnimation(transitionAlfa);
        txtVersion.startAnimation(transitionAlfa);
        progressBar.startAnimation(transitionAlfa);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, 4000);
    }

    private void addDataInDatabase() {

        SharedPreferences newData = getApplicationContext().getSharedPreferences(MoodActivity.STOCKAGE_INFOS, MODE_PRIVATE);
        main_comment = newData.getString(MoodActivity.mComment, "");
        main_color = newData.getString(MoodActivity.mColor, "");
        main_date = newData.getString(MoodActivity.mDate, "");
    }


}