package com.axel.moodtracker.controller;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.axel.moodtracker.R;
import com.axel.moodtracker.model.MoodDbAdapter;
import static com.axel.moodtracker.R.layout.layout_mood_info;

public class HistoryActivity extends AppCompatActivity
{
    private MoodDbAdapter dbHelper;
    //private MyCursorAdapter dataAdapter;
    //private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbHelper = new MoodDbAdapter(this);
        dbHelper.open();
        //Clean all data
        //dbHelper.deleteAllMood();
        //Generate ListView from SQLite Database
        displayListView();
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        getMenuInflater().inflate(layout_mood_info, menu);
        return true;
    }

    private void displayListView()
    {
        Cursor cursor = dbHelper.fetchAllMood();
        // The desired columns to be bound
        String[] columns = new String[]{MoodDbAdapter.COMMENT, MoodDbAdapter.COLOR, MoodDbAdapter.DATE, MoodDbAdapter.TIME};

        // the XML defined views which the data will be bound to
        final int[] to = new int[]{R.id.my_comment, R.id.my_color, R.id.my_time, R.id.my_date};

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        MyCursorAdapter myCursorAdapter = new MyCursorAdapter(this, layout_mood_info, cursor, columns, to, 0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(myCursorAdapter );
    }

    //extend the SimpleCursorAdapter to create a custom class where we
    //can override the getView to change the row colors
    private class MyCursorAdapter extends SimpleCursorAdapter
    {

        public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags)
        {
            super(context, layout, c, from, to, flags);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // ============================= get width of the screen ==========================================
            //get reference to the row
            View view = super.getView(position, convertView, parent);

            RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.relative_layout_info);

            ImageView imageView = (ImageView) view.findViewById(R.id.display_image_comment);
            ListView listView = (ListView) findViewById(R.id.listView1);
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            // Get the state's capital from this row in the database.
            final String moodColor = cursor.getString(cursor.getColumnIndexOrThrow("color"));
            final String moodComment = cursor.getString(cursor.getColumnIndexOrThrow("comment"));

            view.setLayoutParams(new LinearLayout.LayoutParams(resizeWidthAccordingToMood(moodColor), measureHeight()/7));
            relativeLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(HistoryActivity.this, ContactsActivity.class);
                    intent.putExtra("myComment", moodComment );
                    startActivity(intent);
                }
            });

            //check for odd or even to set alternate colors to the row background
            view.setBackgroundColor(Color.parseColor(moodColor));

            if (!moodComment.equals(""))
            {
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(HistoryActivity.this, moodComment, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                //imageView.setVisibility(View.INVISIBLE);
                Toast.makeText(HistoryActivity.this, "Pas de commentaire", Toast.LENGTH_SHORT).show();
            }
            return view;
        }
    }

    private int measureWidth()
    {
        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        else
        {
            screenWidth = display.getWidth();
        }
        return screenWidth;
    }

    private int measureHeight()
    {
        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenHeight;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            Point size = new Point();
            display.getSize(size);
            screenHeight= size.y;
        }
        else
        {
            screenHeight = display.getWidth();
        }
        return screenHeight;
    }

    private int resizeWidthAccordingToMood(String paramColor)
    {
        int newWidth = 0;
        if(paramColor.equals("#AB1A49"))
        {
            newWidth = measureWidth()/5;
        }
        if(paramColor.equals("#808A89"))
        {
            newWidth = measureWidth()/4;
        }
        if(paramColor.equals("#3135D0"))
        {
            newWidth = measureWidth()/3;
        }
        if(paramColor.equals("#55B617"))
        {
            newWidth = measureWidth()/2;
        }
        if(paramColor.equals("#D0E807"))
        {
            newWidth = measureWidth();
        }
        return newWidth;
    }
}