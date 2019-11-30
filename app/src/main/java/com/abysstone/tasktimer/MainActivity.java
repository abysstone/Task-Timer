package com.abysstone.tasktimer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.OnTaskClickListener,
                AddEditActivityFragment.OnSaveClicked{
    private static final String TAG = "MainActivity";

    //whether or not the activity is in 2-pane mode
    //i.e running in landscape on a tablet
    private boolean mTwoPane = false;
    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.task_details_container) != null){
            //the detail container view will be present only in the large-screen layouts (res/value-land and res/values-sq600dp).
            //if this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;
        }

//   testing the content provider
//        String[] projection = { TasksContract.Columns._ID,
//                                TasksContract.Columns.TASKS_NAME,
//                                TasksContract.Columns.TASKS_DESCRIPTION,
//                                TasksContract.Columns.TASKS_SORTORDER};
//        ContentResolver contentResolver = getContentResolver();
//
////  //    insert
////        ContentValues values = new ContentValues();
////        values.put(TasksContract.Columns.TASKS_NAME, "New Task 1");
////        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Description 1");
////        values.put(TasksContract.Columns.TASKS_SORTORDER, 2);
////        Uri uri = contentResolver.insert(TasksContract.CONTENT_URI, values);
////      //  prevent rerun and update em now
////
////     //   update 1
////        ContentValues values = new ContentValues();
////        values.put(TasksContract.Columns.TASKS_NAME, "Content Provider");
////        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Finish setting and testing content provider for project");
////        int count = contentResolver.update(TasksContract.buildTaskUri(4), values, null, null);
////        Log.d(TAG, "onCreate: " + count + " record(s) updated");
////
////        //update multi
////        ContentValues values = new ContentValues();
////        values.put(TasksContract.Columns.TASKS_SORTORDER, "99");
////        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Completed");
////        String selection = TasksContract.Columns.TASKS_SORTORDER + " = " + 2;
////        int count = contentResolver.update(TasksContract.CONTENT_URI, values, selection, null);
////        Log.d(TAG, "onCreate: " + count + "record(s) updated");
////
////        //update aliter : single and multi parameter updates
////        ContentValues values = new ContentValues();
////        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "For deletion");
////        String selection = TasksContract.Columns.TASKS_SORTORDER + " = ?";
////        String[] args = { "0" };
////
////        int count = contentResolver.update(TasksContract.CONTENT_URI, values, selection, args);
////        Log.d(TAG, "onCreate: " + count + "record(s) updated");
////
////   //    deletes
////        int count = contentResolver.delete(TasksContract.buildTaskUri(4), null, null);
////        Log.d(TAG, "onCreate: " + count + "record(s) updated");
////
////        String selection = TasksContract.Columns.TASKS_DESCRIPTION + " = ?";
////        String[] args = { "For deletion" };
////        int count = contentResolver.delete(TasksContract.CONTENT_URI, selection, args);
////        Log.d(TAG, "onCreate: " + count + " record(s) deleted");
//
//
////        Cursor cursor = contentResolver.query(TasksContract.buildTaskUri(2),
//        Cursor cursor = contentResolver.query(TasksContract.CONTENT_URI,
//                projection,
//                null,
//                null,
//                TasksContract.Columns.TASKS_SORTORDER);
//
//    if (cursor != null){
//        Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
//        while (cursor.moveToNext()){
//            for (int i=0; i<cursor.getColumnCount(); i++){
//                Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ": " + cursor.getString(i));
//            }
//            Log.d(TAG, "onCreate: =======================================");
//        }
//    }
////        AppDatabase appDatabase =AppDatabase.getInstance(this);
////        final SQLiteDatabase db = appDatabase.getReadableDatabase();


    }


    @Override
    public void onSaveClicked() {
        Log.d(TAG, "onSaveClicked: starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
        if (fragment != null){
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.remove(fragment);
//            fragmentTransaction.commit();
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
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
        switch (id){
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showDurations:
                break;
            case R.id.menumain_settings:
                break;
            case R.id.menumain_showAbout:
                break;
            case R.id.menumain_generate:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditClick(Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        getContentResolver().delete(TasksContract.buildTaskUri(task.getid()), null, null);
    }

    private void taskEditRequest(Task task){
        Log.d(TAG, "taskEditRequest: starts");
        if (mTwoPane){
            Log.d(TAG, "taskEditRequest: is two-pane mode (tablet)");
            AddEditActivityFragment fragment = new AddEditActivityFragment();

            Bundle arguments = new Bundle();
            arguments.putSerializable(Task.class.getSimpleName(), task);
            fragment.setArguments(arguments);

//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.task_details_container, fragment);
//            fragmentTransaction.commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.task_details_container,fragment)
                    .commit();

        }else{
            Log.d(TAG, "taskEditRequest: in single-pane mode (phone)");
            // in single-pane mode, start the detail activity for the selected item ID.
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if (task != null) { //editing a task
                detailIntent.putExtra(Task.class.getSimpleName(),task);
                startActivity(detailIntent);
            }else {
                startActivity(detailIntent);
            }
        }
    }

}