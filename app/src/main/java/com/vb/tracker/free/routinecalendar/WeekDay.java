package com.vb.tracker.free.routinecalendar;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

public enum WeekDay {

    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private int dayNumber;

    private static final SparseArray<WeekDay> lookup = new SparseArray<>();

    static {
        for (WeekDay weekDay : WeekDay.values()) {
            lookup.put(weekDay.getDayNumber(), weekDay);
        }
    }

    public static WeekDay get(int dayNumber) {
        return lookup.get(dayNumber);
    }

    WeekDay(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getDayNumber() {
        return dayNumber;
    }

}

