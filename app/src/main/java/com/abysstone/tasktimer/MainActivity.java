package com.abysstone.tasktimer;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;




public class MainActivity extends AppCompatActivity
                          implements CursorRecyclerViewAdapter.OnTaskClickListener,
                                     AddEditActivityFragment.OnSaveClicked,
                                     AppDialog.DialogEvents {
    private static final String TAG = "MainActivity";

    //whether or not the activity is in 2-pane mode
    //i.e running in landscape on a tablet
    private boolean mTwoPane = false;
    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";
    public static final int DIALOG_ID_DELETE = 1;
    public static final int DIALOG_ID_CANCEL_EDIT = 2;
    public static final int DIALOG_ID_CANCEL_EDIT_UP = 3;

    private AlertDialog mDialog = null;         // module scope because we need to dismiss it in onStop
                                                // eg when orientation changes to avoid memory leak.

    private Timing mCurrentTiming = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (findViewById(R.id.task_details_container) != null) {
//            //the detail container view will be present only in the large-screen layouts (res/value-land and res/values-sq600dp).
//            //if this view is present, then the activity should be in two-pane mode.
//            mTwoPane = true;
//        }

        mTwoPane = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        Log.d(TAG, "onCreate: twoPane is " + mTwoPane);

        FragmentManager fragmentManager = getSupportFragmentManager();
        // If the AddEditActivity fragment exists, we're editing.
        Boolean editing = fragmentManager.findFragmentById(R.id.task_details_container) != null;
        Log.d(TAG, "onCreate: editing is " + editing);
        
        // We need references to the containers, sowe can show or hide them as necessary.
        // No need to cast them, as we're only calling a method that's available for all views.
        View addEditLayout = findViewById(R.id.task_details_container);
        View mainFragment = findViewById(R.id.fragment);
        
        if (mTwoPane){
            Log.d(TAG, "onCreate: twoPane mode");
            mainFragment.setVisibility(View.VISIBLE);
            addEditLayout.setVisibility(View.VISIBLE);
        }else if(editing){
            Log.d(TAG, "onCreate: single pane, editing");
            // hide the left hand fragment, to make room for editing.
            mainFragment.setVisibility(View.GONE);
        }else {
            Log.d(TAG, "onCreate: single pane, not editing");
            // Show left hand fragment
            mainFragment.setVisibility(View.VISIBLE);
            // Hide the editing frame
            addEditLayout.setVisibility(View.GONE);
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
        if (fragment != null) {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.remove(fragment);
//            fragmentTransaction.commit();
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        View addEditLayout = findViewById(R.id.task_details_container);
        View mainFragment = findViewById(R.id.fragment);

        if (!mTwoPane){
            // we have just removed the editing fragment so hide the frame
            addEditLayout.setVisibility(View.GONE);

            // and that the Mainactivity fragment is visible.
            mainFragment.setVisibility(View.VISIBLE);
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
        switch (id) {
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showDurations:
                break;
            case R.id.menumain_settings:
                break;
            case R.id.menumain_showAbout:
                showAboutDialog();
                break;
            case R.id.menumain_generate:
                break;
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: home button pressed");
                AddEditActivityFragment fragment = (AddEditActivityFragment)
                        getSupportFragmentManager().findFragmentById(R.id.task_details_container);
                if (fragment.canClose()){
                    return super.onOptionsItemSelected(item);
                }else {
                    showConfirmationDialog(DIALOG_ID_CANCEL_EDIT_UP);
                    return true;   // indicate we are handling this..
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAboutDialog(){
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(messageView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "onClick: Entering messageView.onClick showing = " + mDialog.isShowing());   // logd calling a function, might give null pointer ...
                if (mDialog != null && mDialog.isShowing()){
                    mDialog.dismiss();
                }
            }
        });

        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
//        builder.setTitle(R.string.app_name);
//        builder.setIcon(R.mipmap.ic_launcher);

//        messageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: Entering messageView.onClick, showing = " + mDialog.isShowing());
//                if (mDialog != null && mDialog.isShowing()){
//                    mDialog.dismiss();
//                }
//            }
//        });

        TextView tv =(TextView) messageView.findViewById(R.id.about_version);
        tv.setText("v" + BuildConfig.VERSION_NAME);

        mDialog.show();
    }

    @Override
    public void onEditClick(@NonNull Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(@NonNull Task task) {
        Log.d(TAG, "onDeleteClick: starts");

        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_DELETE);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message, task.getId(), task.getName()));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);

        args.putLong("TaskId", task.getId());

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);

//        getContentResolver().delete(TasksContract.buildTaskUri(task.getId()), null, null);

    }

    private void taskEditRequest(Task task) {
        Log.d(TAG, "taskEditRequest: starts");
//        if (mTwoPane) {
        Log.d(TAG, "taskEditRequest: is two-pane mode (tablet)");
        AddEditActivityFragment fragment = new AddEditActivityFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(Task.class.getSimpleName(), task);
        fragment.setArguments(arguments);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "taskEditRequest: twoPane mode");

//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.task_details_container, fragment);
//            fragmentTransaction.commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.task_details_container, fragment)
                .commit();
//    }
//         else {
//            Log.d(TAG, "taskEditRequest: in single-pane mode (phone)");
//            // in single-pane mode, start the detail activity for the selected item ID.
//            Intent detailIntent = new Intent(this, AddEditActivity.class);
//            if (task != null) { //editing a task
//                detailIntent.putExtra(Task.class.getSimpleName(), task);
//                startActivity(detailIntent);
//            } else {
//                startActivity(detailIntent);
//            }
        if (!mTwoPane){
            Log.d(TAG, "taskEditRequest: in single pane mode(phone)");
            // Hide the left hand fragment and show the right hand frame.
            View mainFragment = findViewById(R.id.fragment);
            View addEditLayout = findViewById(R.id.task_details_container);
            mainFragment.setVisibility(View.GONE);
            addEditLayout.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "taskEditRequest: Exiting");
}


    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
        switch (dialogId){
            case DIALOG_ID_DELETE:
                Long taskId = args.getLong("TaskId");
                if (BuildConfig.DEBUG && taskId == 0) throw new AssertionError("Task ID is zero");
                getContentResolver().delete(TasksContract.buildTaskUri(taskId), null, null);
                break;
            case DIALOG_ID_CANCEL_EDIT:
            case DIALOG_ID_CANCEL_EDIT_UP:
                //no action required
                break;
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
        switch (dialogId){
            case DIALOG_ID_DELETE:
                 // no action required
                 break;
            case DIALOG_ID_CANCEL_EDIT:
            case DIALOG_ID_CANCEL_EDIT_UP:
                // If we are editing, remove the fragment, otherwise close the app
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
                if (fragment != null){
                    // we were editing
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragment)
                            .commit();
                    if (mTwoPane){
                        // in landscape so quit only if the back button was used
                        if (dialogId == DIALOG_ID_CANCEL_EDIT) {
                            finish();
                        }
                    }else {
                        // hide the edit container in single pane mode
                        // and make sure the left hand container is visible
                        View addEditLayout = findViewById(R.id.task_details_container);
                        View mainFragment = findViewById(R.id.fragment);
                        // we just removed the editing fragment, so hide the frame
                        addEditLayout.setVisibility(View.GONE);

                        // and make sure the MainActivityFragment is visible
                        mainFragment.setVisibility(View.VISIBLE);
                    }
                }else {
                    // not editing so quit regardless of orientation
                    finish();
                }
                 break;
        }
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment fragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.task_details_container);
        if (fragment == null || fragment.canClose()){
            super.onBackPressed();
        }else {
            // show dialog to get confirmation to quit editing
            showConfirmationDialog(DIALOG_ID_CANCEL_EDIT);
//            AppDialog dialog = new AppDialog();
//            Bundle args = new Bundle();
//            args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT);
//            args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDial_message));
//            args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDiag_positive_caption);
//            args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string. cancelEditDiag_negative_caption);
//
//            dialog.setArguments(args);
//            dialog.show(getSupportFragmentManager(), null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        Log.d(TAG, "onAttachFragment: called, fragment is " + fragment.toString());
        super.onAttachFragment(fragment);
    }

    private void showConfirmationDialog(int dialogId){
        // show dialog to get confirmation to quit editing
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, dialogId);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDial_message));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDiag_positive_caption);
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string. cancelEditDiag_negative_caption);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onTaskLongClick(@NonNull Task task) {
        Log.d(TAG, "onTaskLongClick: called");
        Toast.makeText(this, "Task " + task.getId() + " clicked", Toast.LENGTH_SHORT).show();
        TextView taskName = findViewById(R.id.current_task);
        if (mCurrentTiming != null){
            if (task.getId() == mCurrentTiming.getTaskId().getId()){
                // the current task was tapped a second time, so stop timing
                // TODO saveTiming(mCurrentTiming);
                mCurrentTiming = null;
                taskName.setText(getString(R.string.no_task_message));
            }else {
                // a new task is being timed, so stop the old one first
                // TODO saveTiming(mCurrentTiming);
                mCurrentTiming = new Timing(task);
                taskName.setText("Timing " + mCurrentTiming.getTaskId().getName());
            }
        }else {
            // no task being timed so start timing the new task
            mCurrentTiming = new Timing(task);
            taskName.setText("Timing " + mCurrentTiming.getTaskId().getName());
        }
    }
}