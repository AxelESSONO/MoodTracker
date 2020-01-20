package com.axel.moodtracker.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
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
import com.axel.moodtracker.model.MoodDbAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MoodActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{

    private ImageView mImage;
    private MoodActivity moodActivity;
    private GestureDetectorCompat mDetector;
    private int mImageRessource[] = {R.drawable.a_smiley_disappointed,
            R.drawable.b_smiley_sad,
            R.drawable.c_smiley_normal,
            R.drawable.d_smiley_happy,
            R.drawable.e_smiley_super_happy};
    private int i = 2;
    private String resourceColor [] = {"#AB1A49","#808A89", "#3135D0", "#55B617", "#D0E807" };
    private RelativeLayout relativeLayoutMood;


    private int retreiveImageRessource;
    private String retreive;

    MoodDbAdapter mDatabaseHelper;
    public static final int BUNDLE_EXTRA_IMAGE = 10;
    public static final String BUNDLE_EXTRA_COLOR = "BUNDLE_COLOR";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        this.moodActivity = this;
        // the popup to write the coment
        final ImageView myPopup = findViewById(R.id.add_comment_button);
        //get ImageView
        mImage = (ImageView) findViewById(R.id.list_picture);

        // Instantiate the gesture detector with the
        // application context and an implementation of
        mDetector = new GestureDetectorCompat(this,this);
        relativeLayoutMood = (RelativeLayout) findViewById(R.id.relative_layout_mood);
        mImage = (ImageView) findViewById(R.id.list_picture);
        mImage.setImageResource(mImageRessource[i]);
        relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[i]));
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
        mDatabaseHelper = new MoodDbAdapter(this);
        //moodDbAdapter = new MoodDbAdapter(this);

        //-----------------------------------------------------------------------------------------------------------------
        myPopup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MoodActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.my_popup,null);

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
                mValidateBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String newComment = mComent.getText().toString();
                        String newColor = mColor;

                        // get date and time
                        final String saveCurrentDate;
                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyyy");
                        saveCurrentDate = currentDate.format(calForDate.getTime());

                        //SimpleDateFormat currentTime = new SimpleDateFormat(" HH : mm : ss");
                        //saveCurrentTime = currentTime.format(calForDate.getTime());

                        // Check if field is not empty
                        if(!mComent.getText().toString().isEmpty())
                        {
                            addData(newComment,newColor, saveCurrentDate);
                            mComent.setText("");
                            Toast.makeText(MoodActivity.this,"Votre commentaire a été enrégistré avec succès",Toast.LENGTH_SHORT).show();

                            Intent moodIntent = new Intent();
                            moodIntent.putExtra(String.valueOf(BUNDLE_EXTRA_IMAGE), mImageRessource[i] );
                            moodIntent.putExtra(BUNDLE_EXTRA_COLOR, resourceColor[i]);
                            setResult(RESULT_OK, moodIntent);

                            dialog.dismiss();

                        }
                        else
                        {
                            //Toast.makeText(MoodActivity.this, "Veuillez saisir un commentaire s'il vous plaît !", Toast.LENGTH_SHORT).show();
                            addData(newComment,newColor, saveCurrentDate);
                            mComent.setText("");
                            Toast.makeText(MoodActivity.this,"Humeur enregistrée sans commentaire",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                //To dismiss writing comment
                mCancelBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });
            }
        });
        //----------------------------------------------------------------------------------------------------------------

        //To go to History page
        final ImageView goToHistoryViewBtn = findViewById(R.id.add_history_button);
        goToHistoryViewBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent historyActivity = new Intent(MoodActivity.this, HistoryActivity.class);
                startActivity(historyActivity);
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }


    ////--------------------------------
    public void addData(String comment,String color,String pDate)
    {
        mDatabaseHelper.insertSomeMood(comment,color, pDate);
    }

    /**
     * customizable message
     * @param message
     */
    public void toastMessage(String message){Toast.makeText(this,message,Toast.LENGTH_SHORT).show();}
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {return true;}
        return super.onTouchEvent(event);}

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {return false;}
    @Override
    public boolean onDoubleTap(MotionEvent e) {return false;}
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {return false;}
    @Override
    public boolean onDown(MotionEvent e) {return false;}
    @Override
    public void onShowPress(MotionEvent e) {}
    @Override
    public boolean onSingleTapUp(MotionEvent e) {return false;}
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return false;}
    @Override
    public void onLongPress(MotionEvent e) {}
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        if(velocityY<0)
        {
            if(i == 4)
            {
                mImage.setImageResource(mImageRessource[4]);
                relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[4]));
            }
            else if(i<4)
            {
                mImage.setImageResource(mImageRessource[i+1]);
                relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[i+1]));
                i = i+1;
            }
        }
        if(velocityY > 0)
        {
            if(i == 0)
            {
                mImage.setImageResource(mImageRessource[0]);
                relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[0]));
            }
            else if(i > 0)
            {
                mImage.setImageResource(mImageRessource[i-1]);
                relativeLayoutMood.setBackgroundColor(Color.parseColor(resourceColor[i-1]));
                i = i-1;
            }
        }
        return true;
    }
}
