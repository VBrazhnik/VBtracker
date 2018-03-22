package com.vb.tracker.free.routinecalendar;

import com.vb.tracker.free.datepicker.CalendarDate;

public class RoutineDayCircle {

    private int position;
    private CalendarDate date;
    private int color;
    private boolean isDone;

    public RoutineDayCircle() {
    }

    public RoutineDayCircle(int position, CalendarDate date, int color, boolean isDone) {
        this.position = position;
        this.date = date;
        this.color = color;
        this.isDone = isDone;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(CalendarDate date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
