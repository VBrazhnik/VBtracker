package com.vb.tracker.free.routinecalendar;

import com.vb.tracker.free.datepicker.CalendarDate;

import java.util.ArrayList;
import java.util.List;

public class Routine {

    private long id;
    private String name;
    private CalendarDate startDate;
    private CalendarDate endDate;
    private List<WeekDay> weekdays;
    private int color;
    private String note;
    private List<RoutineDate> days;

    public Routine() {

    }

    public Routine(String name, CalendarDate startDate, CalendarDate endDate, List<WeekDay> weekdays, int color, String note) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekdays = weekdays;
        this.color = color;
        this.note = note;
    }

    public Routine(long id, String name, CalendarDate startDate, CalendarDate endDate, List<WeekDay> weekdays, int color, String note) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekdays = weekdays;
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

    public CalendarDate getStartDate() {
        return startDate;
    }

    public void setStartDate(CalendarDate startDate) {
        this.startDate = startDate;
    }

    public CalendarDate getEndDate() {
        return endDate;
    }

    public void setEndDate(CalendarDate endDate) {
        this.endDate = endDate;
    }

    public List<WeekDay> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<WeekDay> weekdays) {
        this.weekdays = weekdays;
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

    public List<CalendarDate> getDates() {

        List<CalendarDate> dates = new ArrayList<>();

        for (CalendarDate i = new CalendarDate(startDate.getDay(), startDate.getMonth(), startDate.getYear()); CalendarDate.compare(i, endDate) <= 0; ) {

            for (WeekDay weekDay : weekdays) {
                if (i.isThisWeekDay(weekDay)) {
                    CalendarDate date = new CalendarDate(i.getDay(), i.getMonth(), i.getYear());
                    dates.add(date);
                }
            }

            i = i.increment();
        }

        return dates;

    }

    public List<RoutineDate> getDays() {
        return days;
    }

    public void setDays(List<RoutineDate> days) {
        this.days = days;
    }

}