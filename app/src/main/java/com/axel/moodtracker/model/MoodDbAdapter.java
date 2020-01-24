package com.axel.moodtracker.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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
                    " UNIQUE (" + COMMENT +"));";

    // DataBaseHelper -------------------------------------------------------------------
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DATABASE_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }
    //End DataBaseHelper -------------------------------------------------------------------

    public MoodDbAdapter(Context pContext)
    {
        this.mContext = pContext;
    }

    public MoodDbAdapter open() throws SQLException
    {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        if (mDbHelper != null)
        {
            mDbHelper.close();
        }
    }

   public long createMood(String comment, String color, String date)
    {
        //open();

        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(COMMENT, comment);
        initialValues.put(COLOR, color);
        initialValues.put(DATE, date);
        //initialValues.put(TIME, time);
        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllMood()
    {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public void deleteOneLine(String id, String pDate)
    {
        //mDb.delete(SQLITE_TABLE, KEY_ID + " = ?", new String[] {String.valueOf(id)});

        String query = "DELETE FROM " + SQLITE_TABLE + " WHERE "
                + KEY_ID + " = '" + id + "'" +
                " AND " + DATE + " = '" + pDate + "'";

        mDb.execSQL(query);
    }

    public Cursor fetchMoodByColor(String inputText) throws SQLException
    {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)
        {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ID, COMMENT, COLOR, DATE}, null, null, null, null, null);
            //mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ID, COMMENT, COLOR, DATE, TIME}, null, null, null, null, null);
        }
        else
            {
                //mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ID,COMMENT, COLOR, DATE, TIME},
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ID,COMMENT, COLOR, DATE},
                    COLOR + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchMoodByDate(String pDate) throws SQLException
    {
        Log.w(TAG, pDate);
        Cursor mCursor = null;
        if (pDate == null  ||  pDate.length () == 0)
        {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ID, COMMENT, COLOR, DATE}, null, null, null, null, null);
        }
        else
        {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ID,COMMENT, COLOR, DATE},
                    DATE + " like '%" + pDate + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null)
        {
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
       // TIME},

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    public void insertSomeMood(String pComment,String pColor,String pDate)
    {
        createMood(pComment,pColor,pDate);
        //Toast.makeText(mContext, "\n J'affiche le comment:" + pComment + "\n couleur: " + pColor+ "\n la date: "+ pDate+ "\n l'heure: " + pTime, Toast.LENGTH_LONG).show();
    }
    //------------------------------------------------------------------------------
 /*   public static String getDATE(int i) {
        return DATE;
    }*/
}
