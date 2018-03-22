package com.vb.tracker.free;

import android.app.Application;
import android.content.Context;

import com.vb.tracker.free.lock.LockManager;

public class VBtracker extends Application {

    private static VBtracker singleton;

    public static VBtracker getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        LockManager.getInstance().enableAppLock(this);
        singleton = this;
    }

    public static Context getContext() {
        return singleton;
    }

}