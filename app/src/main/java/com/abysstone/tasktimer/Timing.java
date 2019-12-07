package com.abysstone.tasktimer;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

/**
 * Simple Timing object.
 * Sets its start time when created and calculates how long since creation,
 * when setDuration is called
 */

class Timing implements Serializable {
    private static final long serialVersionUID = 2019127L;
    private static final String TAG = Timing.class.getSimpleName();

    private long m_Id;
    private Task mTaskId;
    private long mStartTime;
    private long mDuration;

// time supported in api 26 above, 8version class so utilDate will do the job for us.

    public Timing(Task taskId) {
        mTaskId = taskId;
        // Initialise the start time to now and the duration to zero for a new object.
        Date currentTime = new Date();
        mStartTime = currentTime.getTime() / 1000; // We are only tracking whole seconds, not milliseconds.
        mDuration = 0;
     }


    long getId() {
        return m_Id;
    }

    void setId(long Id) {
        this.m_Id = Id;
    }

    Task getTaskId() {
        return mTaskId;
    }

    void setTaskId(Task taskId) {
        mTaskId = taskId;
    }

    long getStartTime() {
        return mStartTime;
    }

    void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    long getDuration() {
        return mDuration;
    }

    void setDuration() {
        // Calculate the duration from mStartTime to dateTime.
        Date currentTime = new Date();
        mDuration = (currentTime.getTime() / 1000) - mStartTime; // working in seconds(not ms)
        Log.d(TAG, mTaskId.getId() + " - Start time: " + mStartTime + " | Duration: " + mDuration);
    }
}
