package com.abysstone.tasktimer;

import java.io.Serializable;

class Task implements Serializable {
    public static final long serialVersionUID = 20191120L;

    private long m_id;
    private final String mName;
    private final String mDescription;
    private final int mSortOrder;

    public Task(long id, String mName, String mDescription, int mSortOrder) {
        this.m_id = id;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mSortOrder = mSortOrder;
    }

    long getId() {
        return m_id;
    }

    String getName() {
        return mName;
    }

    String getDescription() {
        return mDescription;
    }

    int getSortOrder() {
        return mSortOrder;
    }

    void setid(long id) {
        this.m_id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "m_id=" + m_id +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mSortOrder=" + mSortOrder +
                '}';
    }

}

