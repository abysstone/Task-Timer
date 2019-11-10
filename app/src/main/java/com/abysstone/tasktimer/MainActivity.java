package com.abysstone.tasktimer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] projection = { TasksContract.Columns._ID,
                                TasksContract.Columns.TASKS_NAME,
                                TasksContract.Columns.TASKS_DESCRIPTION,
                                TasksContract.Columns.TASKS_SORTORDER};
        ContentResolver contentResolver = getContentResolver();

//  //    insert
//        ContentValues values = new ContentValues();
//        values.put(TasksContract.Columns.TASKS_NAME, "New Task 1");
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Description 1");
//        values.put(TasksContract.Columns.TASKS_SORTORDER, 2);
//        Uri uri = contentResolver.insert(TasksContract.CONTENT_URI, values);
        //prevent rerun and update em now

        //update 1
//        ContentValues values = new ContentValues();
//        values.put(TasksContract.Columns.TASKS_NAME, "Content Provider");
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Finish setting and testing content provider for project");
//        int count = contentResolver.update(TasksContract.buildTaskUri(4), values, null, null);
//        Log.d(TAG, "onCreate: " + count + " record(s) updated");
//
//        //update multi
//        ContentValues values = new ContentValues();
//        values.put(TasksContract.Columns.TASKS_SORTORDER, "99");
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Completed");
//        String selection = TasksContract.Columns.TASKS_SORTORDER + " = " + 2;
//        int count = contentResolver.update(TasksContract.CONTENT_URI, values, selection, null);
//        Log.d(TAG, "onCreate: " + count + "record(s) updated");

 //       //update aliter : single and multi parameter updates
//        ContentValues values = new ContentValues();
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "For deletion");
//        String selection = TasksContract.Columns.TASKS_SORTORDER + " = ?";
//        String[] args = { "0" };

//        int count = contentResolver.update(TasksContract.CONTENT_URI, values, selection, args);
//        Log.d(TAG, "onCreate: " + count + "record(s) updated");

//   //    deletes
//        int count = contentResolver.delete(TasksContract.buildTaskUri(4), null, null);
//        Log.d(TAG, "onCreate: " + count + "record(s) updated");

        String selection = TasksContract.Columns.TASKS_DESCRIPTION + " = ?";
        String[] args = { "For deletion" };
        int count = contentResolver.delete(TasksContract.CONTENT_URI, selection, args);
        Log.d(TAG, "onCreate: " + count + " record(s) deleted");


//        Cursor cursor = contentResolver.query(TasksContract.buildTaskUri(2),
        Cursor cursor = contentResolver.query(TasksContract.CONTENT_URI,
                projection,
                null,
                null,
                TasksContract.Columns.TASKS_SORTORDER);

    if (cursor != null){
        Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
        while (cursor.moveToNext()){
            for (int i=0; i<cursor.getColumnCount(); i++){
                Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ": " + cursor.getString(i));
            }
            Log.d(TAG, "onCreate: =======================================");
        }
    }
//        AppDatabase appDatabase =AppDatabase.getInstance(this);
//        final SQLiteDatabase db = appDatabase.getReadableDatabase();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
