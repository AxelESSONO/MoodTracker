package com.axel.moodtracker.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MoodDbAdapter
{

    public static final String KEY_ID = "_id";
    public static final String COMMENT = "comment";
    public static final String COLOR = "color";
    public static final String DATE = "date";
    //public static final String TIME = "time";

    private static final String TAG = "MoodAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "My_Mood";
    private static final String SQLITE_TABLE = "Mood";
    private static final int DATABASE_VERSION = 1;

    private final Context mContext;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ID + " integer PRIMARY KEY autoincrement," +
                    COMMENT + "," +
                    COLOR + "," +
                    DATE + "," +
                    " UNIQUE (" + COMMENT + "));";
    public Mood mood;

    // DataBaseHelper -------------------------------------------------------------------
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }

        public int deleteOneLine(String mood_id, String mood_comment, String mood_color, String mood_date){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + SQLITE_TABLE + " WHERE "
                    + KEY_ID + " = '" + mood_id + "'" + ","
                    + COMMENT + " = '" + mood_comment + "'" + ","
                    + COLOR + " = '" + mood_color + "'" +
                    " AND " + DATE + " = '" + mood_date + "'";
            Log.d(TAG, "deleteName: query: " + query);

            Log.d(TAG, "deleteComment: Deleting " + mood_comment + " from database.");
            Log.d(TAG, "deleteColor: Deleting " + mood_color + " from database.");
            Log.d(TAG, "deleteName: Deleting " + mood_date + " from database.");
            db.execSQL(query);
            return 2;
        }


        /**
         * Returns only the ID that matches the name passed in
         * @param date
         * @return
         */
        public Cursor getID(String date){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT " + KEY_ID + " FROM " + SQLITE_TABLE +
                    " WHERE " + DATE + " = '" + date + "'";
            Cursor data = db.rawQuery(query, null);
            return data;
        }

    }
    //End DataBaseHelper -------------------------------------------------------------------

    public MoodDbAdapter(Context pContext) {
        this.mContext = pContext;
    }

    public MoodDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public String getMoodId(String date)
    {
        return mDbHelper.getID(date).toString();
    }

    public boolean deleteLine(String id, String comment, String color, String date)
    {
        int lineDeleted = 0;
        lineDeleted =  mDbHelper.deleteOneLine(id,comment,color,date);
        return lineDeleted > 0;

    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createMood(String comment, String color, String date) {
        //open();

        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(COMMENT, comment);
        initialValues.put(COLOR, color);
        initialValues.put(DATE, date);

        mood = new Mood(comment,color, date);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllMood()
    {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }




    public Cursor fetchMoodByColor(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {
            mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ID, COMMENT, COLOR, DATE}, null, null, null, null, null);
            //mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ID, COMMENT, COLOR, DATE, TIME}, null, null, null, null, null);
        } else {
            //mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ID,COMMENT, COLOR, DATE, TIME},
            mCursor = mDb.query(true, SQLITE_TABLE, new String[]{KEY_ID, COMMENT, COLOR, DATE},
                    COLOR + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchMoodByDate(String pDate) throws SQLException {
        Log.w(TAG, pDate);
        Cursor mCursor = null;
        if (pDate == null || pDate.length() == 0) {
            mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ID, COMMENT, COLOR, DATE}, null, null, null, null, null);
        } else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[]{KEY_ID, COMMENT, COLOR, DATE},
                    DATE + " like '%" + pDate + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllMood()
    {
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]
                        {
                                KEY_ID,
                                COMMENT,
                                COLOR,
                                DATE},
                null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeMood(String pComment, String pColor, String pDate)
    {
        createMood(pComment, pColor, pDate);
    }

}
