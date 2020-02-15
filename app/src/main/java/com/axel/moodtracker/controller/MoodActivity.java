package com.axel.moodtracker.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.axel.moodtracker.R;
import com.axel.moodtracker.model.Mood;
import com.axel.moodtracker.model.MoodDbAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MoodActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    public static final String IMAGE_RESSOURCE = "imageColorToDisplay";
    public static final String IMAGE_COLOR = "imageColor";
    public static final String STOCKAGE_INFOS = "data";
    private ImageView mImage;
    private MoodActivity moodActivity;
    private GestureDetectorCompat mDetector;
    private int mImageRessource[] = {R.drawable.a_smiley_disappointed,
            R.drawable.b_smiley_sad,
            R.drawable.c_smiley_normal,
            R.drawable.d_smiley_happy,
            R.drawable.e_smiley_super_happy};
    private int i = 3;
    private String resourceColor[] = {"#AB1A49", "#808A89", "#3135D0", "#55B617", "#D0E807"};
    private RelativeLayout relativeLayoutMood;

    public static String mComment = null, mColor = null, mDate = null;

    private int retreiveImageRessource;
    private String retreive;

    private MoodDbAdapter mDatabaseHelper;
    public static final int BUNDLE_EXTRA_IMAGE = 10;
    public static final String BUNDLE_EXTRA_COLOR = "BUNDLE_COLOR";
    public SharedPreferences tmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        mDatabaseHelper = new MoodDbAdapter(this);
        //dbHelper = new MoodDbAdapter(this);
        mDatabaseHelper.open();

        this.moodActivity = this;
        // the popup to write the comment
        final ImageView myPopup = findViewById(R.id.add_comment_button);
        //get ImageView
        mImage = (ImageView) findViewById(R.id.list_picture);

        // Instantiate the gesture detector with the
        // application context and an implementation of
        mDetector = new GestureDetectorCompat(this, this);
        relativeLayoutMood = (RelativeLayout) findViewById(R.id.relative_layout_mood);
        mImage = (ImageView) findViewById(R.id.list_picture);

        SharedPreferences data = getApplicationContext().getSharedPreferences(MoodActivity.STOCKAGE_INFOS, MODE_PRIVATE);
        int image = data.getInt(MoodActivity.IMAGE_RESSOURCE, R.drawable.d_smiley_happy);
        String ColorResource = data.getString(MoodActivity.IMAGE_COLOR, "#55B617");

        mImage.setImageResource(image);
        //  mImage.setImageResource(mImageRessource[i]);
        //relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[i]));
        relativeLayoutMood.setBackgroundColor(Color.parseColor(ColorResource));


        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
        //mDatabaseHelper = new MoodDbAdapter(this);
        //moodDbAdapter = new MoodDbAdapter(this);

        //-----------------------------------------------------------------------------------------------------------------

        final SharedPreferences preferences = getSharedPreferences(STOCKAGE_INFOS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();


        myPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MoodActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.my_popup, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                final EditText mComent = (EditText) mView.findViewById(R.id.subTitle);
                TextView mTitle = (TextView) mView.findViewById(R.id.title);
                Button mValidateBtn = (Button) mView.findViewById(R.id.validateButton);
                Button mCancelBtn = (Button) mView.findViewById(R.id.cancelButton);

                final String mColor = resourceColor[i]; // Retreive color
                final String mDate;

                // Clicking on Button Validate
                mValidateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newComment = mComent.getText().toString();
                        String newColor = mColor;

                        // get date and time
                        final String saveCurrentDate;
                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyyy");
                        saveCurrentDate = currentDate.format(calForDate.getTime());

                        Mood mood = new Mood(newComment,newColor,saveCurrentDate);

                        saveAllDataInPreferences(newComment, newColor, saveCurrentDate);

                        // Check if field is not empty
                        if (!(mComent.getText().toString()).isEmpty()) {

                            addData(mood);

                            mComent.setText("");
                            Toast.makeText(MoodActivity.this, "Your mood has been saved successfully", Toast.LENGTH_SHORT).show();
                            editor.putInt(IMAGE_RESSOURCE, mImageRessource[i]);
                            editor.putString(IMAGE_COLOR, resourceColor[i]);
                            editor.commit();

                            Intent moodIntent = new Intent();
                            moodIntent.putExtra(String.valueOf(BUNDLE_EXTRA_IMAGE), mImageRessource[i]);
                            moodIntent.putExtra(BUNDLE_EXTRA_COLOR, resourceColor[i]);

                            setResult(RESULT_OK, moodIntent);
                            dialog.dismiss();

                        } else {
                            addData(mood);
                            //Toast.makeText(MoodActivity.this, newComment, Toast.LENGTH_SHORT).show();
                            mComent.setText("");
                            Toast.makeText(MoodActivity.this, "Mood saved without comment", Toast.LENGTH_SHORT).show();
                            editor.putInt(IMAGE_RESSOURCE, mImageRessource[i]);
                            editor.putString(IMAGE_COLOR, resourceColor[i]);
                            editor.commit();

                            dialog.dismiss();
                        }

                    }
                });
                //To dismiss writing comment
                mCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        //----------------------------------------------------------------------------------------------------------------

        //To go to History page
        final ImageView goToHistoryViewBtn = findViewById(R.id.add_history_button);
        goToHistoryViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent historyActivity = new Intent(MoodActivity.this, HistoryActivity.class);
                startActivity(historyActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public void saveAllDataInPreferences(String pref_comment, String pref_color, String pref_currentDate) {

        tmp = getSharedPreferences(STOCKAGE_INFOS, MODE_PRIVATE);
        SharedPreferences.Editor pref_editor = tmp.edit();
        pref_editor.putString("pref_comment", pref_comment);
        pref_editor.putString("pref_color", pref_color);
        pref_editor.putString("pref_date", pref_currentDate);
        pref_editor.commit();

        mComment = pref_comment;
        mColor = pref_color;
        mDate = pref_currentDate;
    }


    @Override
    protected void onPause() {
        super.onPause();
        //mImage.setImageResource(mImageRessource[3]);
        saveData();
    }

    public Boolean dataExist(String date)
    {




        return true;
    }

    private void saveData() {
        SharedPreferences data = getApplicationContext().getSharedPreferences(MoodActivity.STOCKAGE_INFOS, MODE_PRIVATE);
        int image = data.getInt(MoodActivity.IMAGE_RESSOURCE, R.drawable.d_smiley_happy);
        String resourceColor = data.getString(MoodActivity.IMAGE_COLOR, "#55B617");
        mImage.setImageResource(image);
        relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor));
    }


    @Override
    protected void onStart() {
        super.onStart();
        //mImage.setImageResource(mImageRessource[3]);
        saveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mImage.setImageResource(mImageRessource[i]);
        saveData();
    }

    private int loadSmileyImage(String color) {
        int idImage = 0;
        for(int i = 0; i < resourceColor.length; i++) {
            if(color.equals(resourceColor[i])) {
                idImage = mImageRessource[i];
            }
        }

/*        if (color.equals("#AB1A49")) {
            idImage = R.drawable.a_smiley_disappointed;
        }
        if (color.equals("#808A89")) {
            idImage = R.drawable.b_smiley_sad;
        }
        if (color.equals("#3135D0")) {
            idImage = R.drawable.c_smiley_normal;
        }
        if (color.equals("#55B617")) {
            idImage = R.drawable.d_smiley_happy;
        }
        if (color.equals("#D0E807")) {
            idImage = R.drawable.e_smiley_super_happy;
        }*/
        return idImage;
    }

/*    private void deleteMood(String newComment, String newColor, String saveCurrentDate)
    {
        Cursor cursor = mDatabaseHelper.fetchAllMood();
                //fetchMoodByDate(saveCurrentDate);

        String m_id = cursor.getString(0);
        String m_comment = cursor.getString(1);
        String m_color = cursor.getString(2);
        String m_date = cursor.getString(3);

        Mood mood = new  Mood(m_comment, m_color, saveCurrentDate);
        mDatabaseHelper.deleteOneLine(m_id, m_date);
    }*/



    ////--------------------------------
    public void addData(Mood mood) {
        mDatabaseHelper.insertSomeMood(mood);
    }

    /**
     * customizable message
     *
     * @param message
     */
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (velocityY < 0) {
            if (i == 4) {
                mImage.setImageResource(mImageRessource[4]);
                relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[4]));
            } else if (i < 4) {
                mImage.setImageResource(mImageRessource[i + 1]);
                relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[i + 1]));
                i = i + 1;
            }
        }
        if (velocityY > 0) {
            if (i == 0) {
                mImage.setImageResource(mImageRessource[0]);
                relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[0]));
            } else if (i > 0) {
                mImage.setImageResource(mImageRessource[i - 1]);
                relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[i - 1]));
                i = i - 1;
            }
        }
        return true;
    }
}
