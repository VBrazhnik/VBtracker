package com.vb.tracker.free.todocalendar;

import com.vb.tracker.free.datepicker.CalendarDate;

public class ToDoBadge {

    private int count;
    private CalendarDate date;

    public ToDoBadge() {

    }

    public ToDoBadge(int count, CalendarDate date) {
        this.count = count;
        this.date = date;

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(CalendarDate date) {
        this.date = date;
    }
}
