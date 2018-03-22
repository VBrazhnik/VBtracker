package com.vb.tracker.free.todocalendar;

import com.vb.tracker.free.datepicker.CalendarDate;

public class ToDo {

    private long id;
    private String name;
    private CalendarDate date;
    private int color;
    private String note;
    private boolean isDone = false;

    public ToDo() {

    }

    public ToDo(String name, CalendarDate date, int color, String note) {
        this.name = name;
        this.date = date;
        this.color = color;
        this.note = note;
    }

    public ToDo(long id, String name, CalendarDate date, int color, String note) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.color = color;
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
