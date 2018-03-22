package com.vb.tracker.free.routinecalendar;

import com.vb.tracker.free.datepicker.CalendarDate;

public class RoutineDate {

    private CalendarDate date;
    private boolean isDone;

    public RoutineDate() {
    }

    public RoutineDate(CalendarDate date, boolean isDone) {
        this.date = date;
        this.isDone = isDone;
    }

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(CalendarDate date) {
        this.date = date;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
