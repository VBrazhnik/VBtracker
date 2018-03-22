package com.vb.tracker.free.datepicker;

import com.vb.tracker.free.routinecalendar.WeekDay;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarDate {

    public static String[] months;
    private int day;
    private int month;
    private int year;

    public CalendarDate() {
    }

    public CalendarDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public static CalendarDate getDate(String date) {

        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(5, 7).trim());
        int day = Integer.valueOf(date.substring(8, 10).trim());

        return new CalendarDate(day, month, year);
    }

    public static CalendarDate getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return new CalendarDate(day, month, year);
    }

    public static int getMaxDayOfMonth(int month, int year) {

        Calendar calendar = new GregorianCalendar(year, month, 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String getFormattedMonthAndYear(int month, int year) {
        StringBuilder date = new StringBuilder();
        date.append(months[month]).append(" ").append(year);

        return date.toString();
    }

    public static int compare(CalendarDate first, CalendarDate second) {
        return first.toString().compareTo(second.toString());
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String toString() {

        StringBuilder date = new StringBuilder();
        date.append(year).append(" ");
        if (month < 10) date.append("0");
        date.append(month).append(" ");
        if (day < 10) date.append("0");
        date.append(day);

        return date.toString();
    }

    public String toFormattedString() {
        StringBuilder date = new StringBuilder();
        date.append(months[getMonth()]).append(" ").append(getDay()).append(", ").append(getYear());
        return date.toString();
    }

    public CalendarDate increment() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.add(Calendar.DATE, 1);

        day = c.get(Calendar.DATE);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        return this;
    }

    public boolean isThisWeekDay(WeekDay weekDay) {

        return this.getWeekDay() == weekDay.getDayNumber();
    }

    public boolean equals(CalendarDate date) {
        return (this.day == date.getDay()) && (this.month == date.getMonth()) && (this.year == date.getYear());
    }

    public int getWeekDay() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        c.set(year, month, day);
        int weekday = c.get(Calendar.DAY_OF_WEEK) + 6;
        if (weekday > 7) {
            weekday = weekday - 7;
        }

        return weekday;
    }

}
