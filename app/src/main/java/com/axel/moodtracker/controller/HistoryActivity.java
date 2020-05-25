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
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.axel.moodtracker.utils.FrenchNumberToWords;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import huxy.huxy.huxylab2.HuxyApp;

import static com.axel.moodtracker.R.layout.layout_mood_info;

public class HistoryActivity extends AppCompatActivity {
    private MoodDbAdapter dbHelper;
    private Date date1 = null;
    private Date date2 = null;
    private static long diff = 0;
    private static String duration = "";
    private String main_comment, main_color, main_date;
    private Button delete;
    private ImageView nodataImage;
    private TextView nodataTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbHelper = new MoodDbAdapter(this);
        dbHelper.open();
        //dbHelper.deleteAllMood();
        nodataImage = (ImageView) findViewById(R.id.no_data);
        nodataTxt = (TextView) findViewById(R.id.no_data_txt);
        //Generate ListView from SQLite Database
        displayListView();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        getMenuInflater().inflate(layout_mood_info, menu);
        return true;
    }

    private void displayListView() {
        Cursor cursor = dbHelper.fetchAllMood();


        if (cursor.getCount() == 0) {
            nodataImage.setVisibility(View.VISIBLE);
            nodataTxt.setVisibility(View.VISIBLE);
            Toast.makeText(this, "le nombre: " + cursor.getCount(), Toast.LENGTH_SHORT).show();

        } else {
            String[] columns = new String[]{MoodDbAdapter.DATE};
            final int[] to = new int[]{R.id.my_date};
            MyCursorAdapter myCursorAdapter = new MyCursorAdapter(this, layout_mood_info, cursor, columns, to, 0);
            ListView listView = (ListView) findViewById(R.id.listView1);
            // Assign adapter to ListView
            listView.setAdapter(myCursorAdapter);
        }
    }

    //extend the SimpleCursorAdapter to create a custom class where we
    //can override the getView to change the row colors
    private class MyCursorAdapter extends SimpleCursorAdapter {
        public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // ============================= get width of the screen ==========================================
            //get reference to the row
            View view = super.getView(position, convertView, parent);

            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_info);
            TextView textView1 = (TextView) view.findViewById(R.id.my_date);
            ImageView imageView = (ImageView) view.findViewById(R.id.display_image_comment);
            ListView listView = (ListView) findViewById(R.id.listView1);

            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            // Get the state's capital from this row in the database.
            final String moodColor = cursor.getString(cursor.getColumnIndexOrThrow("color"));
            final String moodComment = cursor.getString(cursor.getColumnIndexOrThrow("comment"));
            String dateMood = null;
            DateFormat df = new SimpleDateFormat("dd MMM yyyy");
            Date date1 = new java.util.Date();
            Date date2 = null;
            try {
                dateMood = cursor.getString(cursor.getColumnIndex(MoodDbAdapter.DATE));
                date2 = df.parse(dateMood);
                diff = (date1.getTime() - date2.getTime()) / 86400000;
                duration = durationInLetter(diff);
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                if (diff <= 7) {
                    textView1.setText(duration);
                } else {
                    textView1.setText("Il y'a " + FrenchNumberToWords.convert(diff) + " jours"); //display duration when duration is greater than 7 days.
                }
            }

            view.setLayoutParams(new LinearLayout.LayoutParams(resizeWidthAccordingToMood(moodColor), measureHeight() / 7));
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryActivity.this, ContactsActivity.class);
                    intent.putExtra("myComment", moodComment);
                    intent.putExtra("colorMood", moodColor);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            //check for odd or even to set alternate colors to the row background
            view.setBackgroundColor(Color.parseColor(moodColor));

            if (!moodComment.equals("")) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Customize Toast message that display comment, and I set background color according to mood color
                        String textColor = "#ffffff";
                        HuxyApp.customToast(HistoryActivity.this, moodComment, moodColor, textColor)
                                .setPositionAndOffSet(Gravity.BOTTOM, 0, 20);
                    }
                });
            } else {
                imageView.setVisibility(View.INVISIBLE);
                //Toast.makeText(HistoryActivity.this, "Pas de commentaire", Toast.LENGTH_SHORT).show();
            }
            return view;
        }

        @Override
        public int getCount() {
            int count = getCursor().getCount();
            return count;
        }
    }

    private int measureWidth() {
        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        } else {
            screenWidth = display.getWidth();
        }
        return screenWidth;
    }

    private int measureHeight() {
        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        } else {
            screenHeight = display.getWidth();
        }
        return screenHeight;
    }

    private int resizeWidthAccordingToMood(String paramColor) {
        String colorIndex[] = {"#AB1A49", "#808A89", "#3135D0", "#55B617", "#D0E807"};
        int widthSet[] = {5, 4, 3, 2, 1};
        int newWidth = 0;
        for (int i = 0; i < colorIndex.length; i++) {
            if (paramColor.equals(colorIndex[i])) {
                newWidth = measureWidth() / widthSet[i];
            }
        }
        return newWidth;
    }

    /**
     * @param last this method convert duration into letter, e.g : if duration = 5 days durationInLetter returns "five"
     * @return value
     */
    private String durationInLetter(long last) {
        String value = "";
        String letters[] = {"Aujourd'hui","Hier", "Avant hier", "Il y'a trois jours", "Il y'a quatre jours", "Il y'a cinq jours", "Il y'a six jours", "Il y'a une semaine"};
        long diff[] = {0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L};
        for (int i = 0; i < diff.length; i++) {
            if (last == diff[i]) {
                value = letters[i];
            }
        }
        return value;
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayListView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        displayListView();
    }
}