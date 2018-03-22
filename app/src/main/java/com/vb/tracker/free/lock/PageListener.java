package com.vb.tracker.free.lock;

import android.app.Activity;

interface PageListener {

	void onActivityCreated(Activity activity);

	void onActivityStarted(Activity activity);

	void onActivityResumed(Activity activity);

	void onActivityPaused(Activity activity);

	void onActivityStopped(Activity activity);

	void onActivitySaveInstanceState(Activity activity);

	void onActivityDestroyed(Activity activity);
}
