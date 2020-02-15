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
                    " UNIQUE (" + DATE + "));";
    public Mood mood;


    // ===================== DatabaseHelper =================================
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

        public Integer deleteContact(int id){
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(SQLITE_TABLE, "_id = ?", new String[] {String.valueOf(id)});
        }



        public void deleteOneLine(String mood_comment,String  mood_color,String mood_date)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            //String mood_comment,String  mood_color,String mood_date
            String query = "DELETE FROM " + SQLITE_TABLE + " WHERE "
                    + MoodDbAdapter.COMMENT + "=?"
                    +  MoodDbAdapter.COLOR + "=?"
                    + " AND " +  MoodDbAdapter.DATE + "=?";


            Log.d(TAG, "deleteName: query: " + query);
            Log.d(TAG, "deleteComment: Deleting " + mood_comment + " from database.");
            Log.d(TAG, "deleteColor: Deleting " + mood_color + " from database.");
            Log.d(TAG, "deleteName: Deleting " + mood_date + " from database.");

            //+ KEY_ID + " = '" +getID(mood_date)+ "'" + ","

            db.execSQL(query);

        }


        public void getID(String date){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE  FROM " + SQLITE_TABLE +
                    " WHERE "
                    + DATE + " =' " + date +"'";

             db.rawQuery(query, null);
        }


    }
    // =================================== End DataBaseHelper ==============================================

    public MoodDbAdapter(Context pContext) {
        this.mContext = pContext;
    }




    public void getRowid(String date)
    {


        mDbHelper = new DatabaseHelper(mContext);
        mDbHelper.getID(date);

      /*  mDbHelper = new DatabaseHelper(mContext);

        String rowID = "";

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long temp;

        Cursor c = db.rawQuery("SELECT * FROM " + SQLITE_TABLE + " WHERE DATE = ?" , new String[] { date });
        if (c != null && c.moveToFirst())
        {
            temp = c.getLong(c.getColumnIndex(KEY_ID));
            rowID = String.valueOf(temp);
            Log.i("----_ROW ID = ", rowID);
        }
        if (c != null && !c.moveToFirst())
        {
            c.moveToFirst();
            rowID = String.valueOf(c.getLong(c.getColumnIndex(KEY_ID)));
            Log.i("CURSOR ERROR", " CURSOR INDEX MOST LIKELY 0");
        }
        else
        {
            c.moveToFirst();
            rowID = String.valueOf(c.getLong(c.getColumnIndex(KEY_ID)));
        }
        return rowID;*/
    }





    public boolean deleteFirstMood(String date)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(SQLITE_TABLE,KEY_ID + "= ?", new String[] { date}) > 0;

    }









    public MoodDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }


    public boolean deleteLine(String comment, String color, String date)
    {
        int lineDeleted = 0;
        //lineDeleted =  mDbHelper.deleteOneLine(comment,color,date);
        return lineDeleted > 0;

    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public boolean createMood(Mood mood) {
        //open();

        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();

        // Check if this date exists in database

        ContentValues initialValues = new ContentValues();
        initialValues.put(COMMENT, mood.getComment());
        initialValues.put(COLOR, mood.getColor());
        initialValues.put(DATE, mood.getDate());

        //return mDb.insert(SQLITE_TABLE, null, initialValues);

        long result = mDb.insert(SQLITE_TABLE, null, initialValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    public boolean updateMood(Mood mood, int id){
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMMENT, mood.getComment());
        contentValues.put(COLOR, mood.getColor());
        contentValues.put(DATE, mood.getDate());

        int update = mDb.update(SQLITE_TABLE, contentValues, KEY_ID + " = ? ", new String[] {String.valueOf(id)} );

        if(update != 1) {
            return false;
        }
        else{
            return true;
        }
    }



    public Cursor getAllMood(){
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return mDb.rawQuery("SELECT * FROM " + SQLITE_TABLE, null);
    }



    public Cursor getMyMoodID(Mood mood){
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();

        String sql = "SELECT * FROM " + SQLITE_TABLE  +
                " WHERE " + COMMENT + " = '" + mood.getComment() + "'" +
                " AND " + DATE + " = '" + mood.getDate() + "'";
        return mDb.rawQuery(sql, null);
    }

    public Integer deleteAMood(String id){
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();

        return mDb.delete(SQLITE_TABLE, "_id = ?", new String[] {String.valueOf(id)});
    }





    public boolean deleteAllMood()
    {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public void deleteMood(String mood_comment, String mood_color, String mood_date)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        databaseHelper.deleteOneLine(mood_comment, mood_color, mood_date);
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

    public void insertSomeMood(Mood mood)
    {
        createMood(mood);
    }
}
