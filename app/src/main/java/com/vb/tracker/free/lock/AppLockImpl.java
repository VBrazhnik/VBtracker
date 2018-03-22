package com.vb.tracker.free.lock;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class AppLockImpl extends AppLock implements PageListener {

	private static final String PASSWORD_PREFERENCE_KEY = "password";
	private static final String PASSWORD_SALT = "7xn7@c$";

	private SharedPreferences settings;

	private int liveCount;
	private int visibleCount;

	private long lastActive;

	public AppLockImpl(Application app) {
		super();
		this.settings = PreferenceManager.getDefaultSharedPreferences(app);
		this.liveCount = 0;
		this.visibleCount = 0;
	}

	public void enable() {
		BaseActivity.setListener(this);
	}

	public void disable() {
		BaseActivity.setListener(null);
	}

	public boolean checkPassword(String password) {
		password = PASSWORD_SALT + password + PASSWORD_SALT;
		password = Encryptor.getSHA1(password);
		String storedPassword = "";

		if (settings.contains(PASSWORD_PREFERENCE_KEY)) {
			storedPassword = settings.getString(PASSWORD_PREFERENCE_KEY, "");
		}

		return password.equalsIgnoreCase(storedPassword);
	}

	public boolean setPassword(String password) {
		SharedPreferences.Editor editor = settings.edit();

		if (password == null) {
			editor.remove(PASSWORD_PREFERENCE_KEY);
			editor.commit();
			this.disable();
		} else {
			password = PASSWORD_SALT + password + PASSWORD_SALT;
			password = Encryptor.getSHA1(password);
			editor.putString(PASSWORD_PREFERENCE_KEY, password);
			editor.commit();
			this.enable();
		}

		return true;
	}

	public boolean isPasswordSet() {
		return settings.contains(PASSWORD_PREFERENCE_KEY);
	}

	private boolean isIgnoredActivity(Activity activity) {
		String clazzName = activity.getClass().getName();
		return ignoredActivities.contains(clazzName);
	}

	private boolean shouldLockScreen(Activity activity) {

		if (activity instanceof AppLockActivity) {
			AppLockActivity ala = (AppLockActivity) activity;
			if (ala.getType() == AppLock.UNLOCK_PASSWORD) {
				return false;
			}
		}

		if (!isPasswordSet()) {
			return false;
		}

		long passedTime = System.currentTimeMillis() - lastActive;
		if (lastActive > 0 && passedTime <= lockTimeOut) {
			return false;
		}

		if (visibleCount > 1) {
			return false;
		}

		return true;
	}

	@Override
	public void onActivityResumed(Activity activity) {

		if (isIgnoredActivity(activity)) {
			return;
		}

		if (shouldLockScreen(activity)) {
			Intent intent = new Intent(activity.getApplicationContext(),
					AppLockActivity.class);
			intent.putExtra(AppLock.TYPE, AppLock.UNLOCK_PASSWORD);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.getApplication().startActivity(intent);
		}

		lastActive = 0;
	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityCreated(Activity activity) {

		if (isIgnoredActivity(activity)) {
			return;
		}

		liveCount++;
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		if (isIgnoredActivity(activity)) {
			return;
		}

		liveCount--;
		if (liveCount == 0) {
			lastActive = System.currentTimeMillis();
		}
	}

	@Override
	public void onActivityStarted(Activity activity) {

		if (isIgnoredActivity(activity)) {
			return;
		}

		visibleCount++;
	}

	@Override
	public void onActivityStopped(Activity activity) {

		if (isIgnoredActivity(activity)) {
			return;
		}

		visibleCount--;
		if (visibleCount == 0) {
			lastActive = System.currentTimeMillis();
		}
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity) {
	}
}
