package com.vb.tracker.free.routinecalendar;

import com.vb.tracker.free.datepicker.CalendarDate;

public class RoutineDay {

    private long id;
    private CalendarDate date;
    private boolean isDone;
    private Routine routine;

    public RoutineDay() {
    }

    public RoutineDay(CalendarDate date, Routine routine) {
        this.date = date;
        this.routine = routine;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }
}
