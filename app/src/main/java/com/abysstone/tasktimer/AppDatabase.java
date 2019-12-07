package com.abysstone.tasktimer;

//basic database class for the application
//the only class that should use this is the {@link AppProvider}.


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class AppDatabase extends SQLiteOpenHelper {

    private static final String TAG = "AppDatabase";
    public static final String DATABASE_NAME = "TaskTimer.db";
    public static final int DATABASE_VERSION = 2;

    //Implement AppDatabase as a Singelton
    private static AppDatabase instance = null;

    private AppDatabase(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase: constructor");
    }

    /**
     * Get an instance of the app's singleton database helper object
     *
     * @param context the content provider context
     * @return a SQLite database helper object
     */
    static AppDatabase getInstance(Context context){
        if (instance == null){
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");
        String sSQL;    // Use a string variable to facilitate logging
//        sSQL = "CREATE TABLE Tasks (_id INTEGER PRIMARY KEY NOT NULL, Name TEXT NOT NULL, DESCRIPTION TEXT, SortOrder INTEGER)";
        sSQL = "CREATE TABLE " + TasksContract.TABLE_NAME + " ("
                + TasksContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + TasksContract.Columns.TASKS_NAME + " TEXT NOT NULL, "
                + TasksContract.Columns.TASKS_DESCRIPTION + " TEXT, "
                + TasksContract.Columns.TASKS_SORTORDER + " INTEGER);";

        Log.d(TAG, "onCreate: sSQL");
        db.execSQL(sSQL);



        Log.d(TAG, "onCreate: Ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch (oldVersion){
            case 1:
                //upgrade logic from version 1
                addTimingsTable(db);
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: " + newVersion);
        }
        Log.d(TAG, "onUpgrade: ends");
    }

    private void addTimingsTable(SQLiteDatabase db){
        String sSQL = "CREATE TABLE " + TimingsContract.TABLE_NAME + " ("
                + TimingsContract.Columns._ID + "INTEGER PRIMARY KEY NOT NULL, "
                + TimingsContract.Columns.TIMINGS_TASK_ID + " INTEGER NOT NULL, "
                + TimingsContract.Columns.TIMINGS_START_TIME + " INTEGER, "
                + TimingsContract.Columns.TIMINGS_DURATION + " INTEGER);";

        Log.d(TAG, "onCreate: sSQL");
        db.execSQL(sSQL);

        sSQL = "CREATE TRIGGER Remove_Task"
                + " AFTER DELETE ON " + TasksContract.TABLE_NAME
                + " FOR EACH ROW"
                + " BEGIN"
                + " DELETE FROM " + TimingsContract.TABLE_NAME
                + " WHERE " + TimingsContract.Columns.TIMINGS_TASK_ID + " = OLD." + TasksContract.Columns._ID + ";"
                + " END;";

        Log.d(TAG, "onCreate: sSQL");
        db.execSQL(sSQL);
    }
}
