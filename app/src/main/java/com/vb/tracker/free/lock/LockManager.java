package com.vb.tracker.free.lock;

import android.app.Application;

public class LockManager {

    private volatile static LockManager instance;
    private AppLock curAppLocker;

    public static LockManager getInstance() {
        synchronized (LockManager.class) {
            if (instance == null) {
                instance = new LockManager();
            }
        }
        return instance;
    }

    public void enableAppLock(Application app) {
        if (curAppLocker == null) {
            curAppLocker = new AppLockImpl(app);
        }
        curAppLocker.enable();
    }

    public boolean isAppLockEnabled() {
        return !(curAppLocker == null);
    }

    public void setAppLock(AppLock appLocker) {
        if (curAppLocker != null) {
            curAppLocker.disable();
        }
        curAppLocker = appLocker;
    }

    public AppLock getAppLock() {
        return curAppLocker;
    }
}
