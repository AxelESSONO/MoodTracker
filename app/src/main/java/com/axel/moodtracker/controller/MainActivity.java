package com.axel.moodtracker.controller;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.axel.moodtracker.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private LinearLayout linearLayout;
    private TextView txtAppName;
    private TextView txtVersion;
    private ProgressBar progressBar;
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainIntent = new Intent(MainActivity.this, MoodActivity.class);
        initView();
        getData();
        setTransition();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, Constants.FOUR_SECOND);
    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.RECENT_MOOD, MODE_PRIVATE);
        int colorByPosition = sharedPreferences.getInt(Constants.COLOR_BY_POSITION, getResources().getIntArray(R.array.colorPagesViewPager)[3]);
        int moodImageByPosition = sharedPreferences.getInt(Constants.MOOD_IMAGE_BY_POSITION, R.drawable.d_smiley_happy);
        upDateView(colorByPosition, moodImageByPosition);
    }

    private void upDateView(int colorByPosition, int moodImageByPosition) {
        linearLayout.setBackgroundColor(colorByPosition);
        imageView.setImageResource(moodImageByPosition);
    }

    private void setTransition() {
        Animation transitionAlfa = AnimationUtils.loadAnimation(this, R.anim.transition_alfa);
        imageView.startAnimation(transitionAlfa);
        txtAppName.startAnimation(transitionAlfa);
        txtVersion.startAnimation(transitionAlfa);
        progressBar.startAnimation(transitionAlfa);
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.smiley_icon);
        txtAppName = (TextView) findViewById(R.id.name_of_app);
        txtVersion = (TextView) findViewById(R.id.version_name);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_main);
    }
}