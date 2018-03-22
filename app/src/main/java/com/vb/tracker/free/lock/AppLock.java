package com.vb.tracker.free.lock;

import java.util.HashSet;

public abstract class AppLock {

	public static final int ENABLE_PASSWORD = 0;
	public static final int DISABLE_PASSWORD = 1;
	public static final int CHANGE_PASSWORD = 2;
	public static final int UNLOCK_PASSWORD = 3;

	public static final String MESSAGE = "message";
	public static final String TYPE = "type";

	private static final int DEFAULT_TIMEOUT = 0;

	protected int lockTimeOut;
    protected HashSet<String> ignoredActivities;

	AppLock() {
		this.ignoredActivities = new HashSet<>();
		this.lockTimeOut = DEFAULT_TIMEOUT;
	}

	public void addIgnoredActivity(Class<?> clazz) {
		String clazzName = clazz.getName();
		this.ignoredActivities.add(clazzName);
	}

	public void removeIgnoredActivity(Class<?> clazz) {
		String clazzName = clazz.getName();
		this.ignoredActivities.remove(clazzName);
	}

    public abstract void enable();

	public abstract void disable();

	public abstract boolean setPassword(String password);

	public abstract boolean checkPassword(String password);

	public abstract boolean isPasswordSet();
}
