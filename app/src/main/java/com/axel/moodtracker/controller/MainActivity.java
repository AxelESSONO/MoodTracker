package com.axel.moodtracker.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    //private SharedPreferences mPreferences;
    //public static final int MOOD_ACTIVITY_REQUEST_CODE = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mPreferences = getPreferences(MODE_PRIVATE);
        //mPreferences.edit().putString(PREF_KEY_IMAGE, mood.getColor()).apply();

        mainIntent = new Intent(MainActivity.this, MoodActivity.class);

        imageView = (ImageView) findViewById(R.id.smiley_icon);
        txtAppName = (TextView) findViewById(R.id.name_of_app);
        txtVersion = (TextView) findViewById(R.id.version_name);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_main);

        SharedPreferences data = getApplicationContext().getSharedPreferences(MoodActivity.STOCKAGE_INFOS, MODE_PRIVATE);
        int image = data.getInt(MoodActivity.IMAGE_RESSOURCE, R.drawable.d_smiley_happy);
        String resourceColor = data.getString(MoodActivity.IMAGE_COLOR, "#55B617");

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


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moodActivity = new Intent(MainActivity.this, MoodActivity.class);

                //moodActivity.putExtra("moodActivityImage", image);
                //  moodActivity.putExtra("moodActivityColor", resourceColor);
                startActivity(moodActivity);
            }
        });
    }


    private int loadSmileyImage(String image) {
        int idImage = 0;
        if (image.equals("#AB1A49")) {
            idImage = R.drawable.a_smiley_disappointed;
        }
        if (image.equals("#808A89")) {
            idImage = R.drawable.b_smiley_sad;
        }
        if (image.equals("#3135D0")) {
            idImage = R.drawable.c_smiley_normal;
        }
        if (image.equals("#55B617")) {
            idImage = R.drawable.d_smiley_happy;
        }
        if (image.equals("#D0E807")) {
            idImage = R.drawable.e_smiley_super_happy;
        }
        return idImage;
    }



/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (MOOD_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode)
        {
            // Fetch the score from the Intent
            int image = data.getIntExtra(String.valueOf(MoodActivity.BUNDLE_EXTRA_IMAGE), 0);
            mPreferences.edit().putInt(PREF_KEY_IMAGE, image).apply();
            String color = data.getStringExtra(MoodActivity.BUNDLE_EXTRA_COLOR);
            mPreferences.edit().putString(PREF_KEY_COLOR, color).apply();

            imageView.setImageResource(image);
            linearLayout.setBackgroundColor(Integer.parseInt(color));

            //mPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();
        }
    }*/
}