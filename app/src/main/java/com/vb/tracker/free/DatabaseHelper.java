package com.vb.tracker.free;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vb.tracker.R;
import com.vb.tracker.free.colorpicker.ColorPalette;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.routinecalendar.Routine;
import com.vb.tracker.free.routinecalendar.RoutineDate;
import com.vb.tracker.free.routinecalendar.RoutineDay;
import com.vb.tracker.free.routinecalendar.RoutineDayCircle;
import com.vb.tracker.free.routinecalendar.WeekDay;
import com.vb.tracker.free.todocalendar.ToDo;
import com.vb.tracker.free.todocalendar.ToDoBadge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "trackerManager";

    // Table Names
    private static final String TABLE_TODO = "todos";
    private static final String TABLE_ROUTINE = "routines";
    private static final String TABLE_ROUTINE_WEEKDAYS = "routine_weekdays";
    private static final String TABLE_ROUTINE_DAYS = "routine_days";

    //Table ToDos
    private static final String TODO_ID = "todo_id";
    private static final String TODO_NAME = "todo_name";
    private static final String TODO_COLOR = "todo_color";
    private static final String TODO_DATE = "todo_date";
    private static final String TODO_NOTE = "todo_note";
    private static final String TODO_IS_DONE = "todo_isDone";

    private static final String TODO_BADGE = "todo_badge";
    private static final String TODO_BADGE_RESULT = "todo_badgeResult";
    private static final String TODO_BADGE_DATE = "todo_badgeDate";

    //Table Routines
    private static final String ROUTINE_ID = "routine_id";
    private static final String ROUTINE_NAME = "routine_name";
    private static final String ROUTINE_COLOR = "routine_color";
    private static final String ROUTINE_START_DATE = "routine_start_date";
    private static final String ROUTINE_END_DATE = "routine_end_date";
    private static final String ROUTINE_NOTE = "routine_note";

    //Table RoutineDays
    private static final String ROUTINE_DAYS_DAY_ID = "day_id";
    private static final String ROUTINE_DAYS_ROUTINE_ID = "day_routine_id";
    private static final String ROUTINE_DAYS_DATE = "day_date";
    private static final String ROUTINE_DAYS_IS_DONE = "day_isDone";

    //Table RoutineWeekDays
    private static final String ROUTINE_WEEKDAYS_ROUTINE_ID = "weekday_routine_id";
    private static final String ROUTINE_WEEKDAYS_WEEKDAY_ID = "weekday_id";

    //Create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE " + TABLE_TODO + "("
            + TODO_ID + " INTEGER PRIMARY KEY,"
            + TODO_NAME + " TEXT,"
            + TODO_COLOR + " INTEGER,"
            + TODO_DATE + " DATE,"
            + TODO_NOTE + " TEXT,"
            + TODO_IS_DONE + " INTEGER)";

    private static final String CREATE_TABLE_ROUTINE = "CREATE TABLE " + TABLE_ROUTINE + "("
            + ROUTINE_ID + " INTEGER PRIMARY KEY,"
            + ROUTINE_NAME + " TEXT,"
            + ROUTINE_COLOR + " INTEGER,"
            + ROUTINE_START_DATE + " DATE,"
            + ROUTINE_END_DATE + " DATE,"
            + ROUTINE_NOTE + " TEXT)";

    private static final String CREATE_TABLE_ROUTINE_WEEKDAYS = "CREATE TABLE " + TABLE_ROUTINE_WEEKDAYS + "("
            + ROUTINE_WEEKDAYS_ROUTINE_ID + " INTEGER,"
            + ROUTINE_WEEKDAYS_WEEKDAY_ID + " INTEGER)";

    private static final String CREATE_TABLE_ROUTINE_DAYS = "CREATE TABLE " + TABLE_ROUTINE_DAYS + "("
            + ROUTINE_DAYS_DAY_ID + " INTEGER PRIMARY KEY,"
            + ROUTINE_DAYS_ROUTINE_ID + " INTEGER,"
            + ROUTINE_DAYS_DATE + " DATE,"
            + ROUTINE_DAYS_IS_DONE + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_ROUTINE);
        db.execSQL(CREATE_TABLE_ROUTINE_WEEKDAYS);
        db.execSQL(CREATE_TABLE_ROUTINE_DAYS);

        ToDo todo = new ToDo();
        
        todo.setName(VBtracker.getContext().getString(R.string.todo_name_example));
        todo.setDate(CalendarDate.getCurrentDate());
        todo.setColor(ColorPalette.RED.getColor());
        todo.setNote(VBtracker.getContext().getString(R.string.todo_note_example));

        ContentValues todoValues = new ContentValues();
        todoValues.put(TODO_NAME, todo.getName());
        todoValues.put(TODO_COLOR, todo.getColor());
        todoValues.put(TODO_DATE, todo.getDate().toString());
        todoValues.put(TODO_NOTE, todo.getNote());
        todoValues.put(TODO_IS_DONE, todo.isDone());

        db.insert(TABLE_TODO, null, todoValues);

        Routine routine = new Routine();
        routine.setName(VBtracker.getContext().getString(R.string.routine_name_example));
        routine.setStartDate(CalendarDate.getCurrentDate());
        routine.setEndDate(CalendarDate.getCurrentDate().increment().increment().increment().increment().increment().increment().increment());
        routine.setColor(ColorPalette.BLUE.getColor());

        List<WeekDay> weekdays = new ArrayList<>();
        weekdays.add(WeekDay.MONDAY);
        weekdays.add(WeekDay.TUESDAY);
        weekdays.add(WeekDay.WEDNESDAY);
        weekdays.add(WeekDay.THURSDAY);
        weekdays.add(WeekDay.FRIDAY);
        weekdays.add(WeekDay.SATURDAY);
        weekdays.add(WeekDay.SUNDAY);

        routine.setWeekdays(weekdays);
        routine.setNote(VBtracker.getContext().getString(R.string.routine_note_example));

        ContentValues routineValues = new ContentValues();
        routineValues.put(ROUTINE_NAME, routine.getName());
        routineValues.put(ROUTINE_COLOR, routine.getColor());
        routineValues.put(ROUTINE_START_DATE, routine.getStartDate().toString());
        routineValues.put(ROUTINE_END_DATE, routine.getEndDate().toString());
        routineValues.put(ROUTINE_NOTE, routine.getNote());

        long routineId = db.insert(TABLE_ROUTINE, null, routineValues);

        List<WeekDay> routineWeekdays = routine.getWeekdays();

        for (WeekDay weekday : routineWeekdays) {

            ContentValues weekValues = new ContentValues();
            weekValues.put(ROUTINE_WEEKDAYS_ROUTINE_ID, routineId);
            weekValues.put(ROUTINE_WEEKDAYS_WEEKDAY_ID, weekday.getDayNumber());

            db.insert(TABLE_ROUTINE_WEEKDAYS, null, weekValues);
        }

        for (CalendarDate date : routine.getDates()) {

            ContentValues dayValues = new ContentValues();
            dayValues.put(ROUTINE_DAYS_ROUTINE_ID, routineId);
            dayValues.put(ROUTINE_DAYS_DATE, date.toString());
            dayValues.put(ROUTINE_DAYS_IS_DONE, 0);

            db.insert(TABLE_ROUTINE_DAYS, null, dayValues);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTINE_WEEKDAYS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTINE_DAYS);

        onCreate(db);
    }

    //ToDos methods
    public long createToDo(ToDo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TODO_NAME, todo.getName());
        values.put(TODO_COLOR, todo.getColor());
        values.put(TODO_DATE, todo.getDate().toString());
        values.put(TODO_NOTE, todo.getNote());
        values.put(TODO_IS_DONE, todo.isDone());

        return db.insert(TABLE_TODO, null, values);
    }

    public ToDo getToDo(int id) {

        ToDo todo = new ToDo();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TODO + " WHERE " + TODO_ID + " = " + id;

        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();

            todo.setId(c.getInt((c.getColumnIndex(TODO_ID))));
            todo.setName(c.getString(c.getColumnIndex(TODO_NAME)));
            todo.setColor(c.getInt(c.getColumnIndex(TODO_COLOR)));
            todo.setDate(CalendarDate.getDate(c.getString(c.getColumnIndex(TODO_DATE))));
            todo.setNote(c.getString(c.getColumnIndex(TODO_NOTE)));
            boolean done = (c.getInt(c.getColumnIndex(TODO_IS_DONE)) == 1);
            todo.setDone(done);

            c.close();
        }

        return todo;
    }

    public List<ToDo> getUncompletedToDos(CalendarDate date) {

        List<ToDo> todos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TODO + " WHERE "
                + TODO_DATE + " = '" + date.toString() + "' AND " + TODO_IS_DONE + " = 0";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                ToDo todo = new ToDo();
                todo.setId(c.getInt((c.getColumnIndex(TODO_ID))));
                todo.setName(c.getString(c.getColumnIndex(TODO_NAME)));
                todo.setColor(c.getInt(c.getColumnIndex(TODO_COLOR)));
                todo.setDate(CalendarDate.getDate(c.getString(c.getColumnIndex(TODO_DATE))));
                todo.setNote(c.getString(c.getColumnIndex(TODO_NOTE)));
                boolean done = (c.getInt(c.getColumnIndex(TODO_IS_DONE)) == 1);
                todo.setDone(done);
                todos.add(todo);
            } while (c.moveToNext());
        }

        c.close();

        return todos;
    }

    public List<ToDo> getToDos(CalendarDate date) {

        List<ToDo> todos = new ArrayList<>();

        todos.addAll(getUncompletedToDos(date));

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TODO + " WHERE "
                + TODO_DATE + " = '" + date.toString() + "' AND " + TODO_IS_DONE + " = 1";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                ToDo todo = new ToDo();
                todo.setId(c.getInt((c.getColumnIndex(TODO_ID))));
                todo.setName(c.getString(c.getColumnIndex(TODO_NAME)));
                todo.setColor(c.getInt(c.getColumnIndex(TODO_COLOR)));
                todo.setDate(CalendarDate.getDate(c.getString(c.getColumnIndex(TODO_DATE))));
                todo.setNote(c.getString(c.getColumnIndex(TODO_NOTE)));
                boolean done = (c.getInt(c.getColumnIndex(TODO_IS_DONE)) == 1);
                todo.setDone(done);
                todos.add(todo);
            } while (c.moveToNext());
        }

        c.close();

        return todos;
    }

    public int updateToDo(ToDo todo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TODO_ID, todo.getId());
        values.put(TODO_NAME, todo.getName());
        values.put(TODO_COLOR, todo.getColor());
        values.put(TODO_DATE, todo.getDate().toString());
        values.put(TODO_NOTE, todo.getNote());
        values.put(TODO_IS_DONE, todo.isDone());

        return db.update(TABLE_TODO, values, TODO_ID + " = ?", new String[]{String.valueOf(todo.getId())});
    }

    public long deleteToDo(long id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_TODO, TODO_ID + " = ? ", new String[]{String.valueOf(id)});
    }

    public List<ToDoBadge> getBadges(CalendarDate start, CalendarDate finish) {

        List<ToDoBadge> badges = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT TABLE1." + TODO_DATE + " AS " + TODO_BADGE_DATE + ", IFNULL(TABLE1." + TODO_BADGE + " - IFNULL(TABLE2." + TODO_BADGE + ", 0), 0) AS " + TODO_BADGE_RESULT
                + " FROM ((SELECT COUNT(" + TODO_DATE + ")" + TODO_BADGE + ", " + TODO_DATE + " FROM "
                + TABLE_TODO + " WHERE " + TODO_DATE + " BETWEEN '" + start.toString() + "' AND '" + finish.toString()
                + "' GROUP BY " + TODO_DATE + ") AS TABLE1 LEFT JOIN (SELECT COUNT(" + TODO_DATE + ") " + TODO_BADGE + ", " + TODO_DATE
                + " FROM " + TABLE_TODO + " WHERE " + TODO_DATE + " BETWEEN '" + start.toString() + "' AND '" + finish.toString()
                + "' AND " + TODO_IS_DONE + " = 1 GROUP BY " + TODO_DATE + ") AS TABLE2 ON TABLE1."
                + TODO_DATE + " = TABLE2." + TODO_DATE + ")";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                ToDoBadge toDoBadge = new ToDoBadge();
                toDoBadge.setCount(c.getInt((c.getColumnIndex(TODO_BADGE_RESULT))));
                toDoBadge.setDate(CalendarDate.getDate(c.getString(c.getColumnIndex(TODO_BADGE_DATE))));
                badges.add(toDoBadge);
            } while (c.moveToNext());
        }

        c.close();

        return badges;
    }

    //Routines methods
    public long createRoutine(Routine routine) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ROUTINE_NAME, routine.getName());
        values.put(ROUTINE_COLOR, routine.getColor());
        values.put(ROUTINE_START_DATE, routine.getStartDate().toString());
        values.put(ROUTINE_END_DATE, routine.getEndDate().toString());
        values.put(ROUTINE_NOTE, routine.getNote());

        long routineId = db.insert(TABLE_ROUTINE, null, values);

        List<WeekDay> weekdays = routine.getWeekdays();

        for (WeekDay weekday : weekdays) {

            ContentValues weekValues = new ContentValues();
            weekValues.put(ROUTINE_WEEKDAYS_ROUTINE_ID, routineId);
            weekValues.put(ROUTINE_WEEKDAYS_WEEKDAY_ID, weekday.getDayNumber());

            db.insert(TABLE_ROUTINE_WEEKDAYS, null, weekValues);
        }

        for (CalendarDate date : routine.getDates()) {

            ContentValues dayValues = new ContentValues();
            dayValues.put(ROUTINE_DAYS_ROUTINE_ID, routineId);
            dayValues.put(ROUTINE_DAYS_DATE, date.toString());
            dayValues.put(ROUTINE_DAYS_IS_DONE, 0);

            db.insert(TABLE_ROUTINE_DAYS, null, dayValues);
        }

        return routineId;
    }

    public int updateRoutine(Routine routine) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ROUTINE_WEEKDAYS, ROUTINE_WEEKDAYS_ROUTINE_ID + " = ? ",
                new String[]{String.valueOf(routine.getId())});

        List<WeekDay> weekdays = routine.getWeekdays();

        for (WeekDay weekday : weekdays) {
            ContentValues weekValues = new ContentValues();
            weekValues.put(ROUTINE_WEEKDAYS_ROUTINE_ID, routine.getId());
            weekValues.put(ROUTINE_WEEKDAYS_WEEKDAY_ID, weekday.getDayNumber());

            db.insert(TABLE_ROUTINE_WEEKDAYS, null, weekValues);
        }

        Map<CalendarDate, Integer> oldDays = new HashMap<>();

        String query = "SELECT " + ROUTINE_DAYS_DAY_ID + ", " + ROUTINE_DAYS_DATE
                + " FROM " + TABLE_ROUTINE_DAYS + " WHERE " + ROUTINE_DAYS_ROUTINE_ID + " = "
                + routine.getId();

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                oldDays.put(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_DAYS_DATE))), c.getInt(c.getColumnIndex(ROUTINE_DAYS_DAY_ID)));
            } while (c.moveToNext());
        }

        c.close();

        List<CalendarDate> newDays = routine.getDates();

        Iterator it = oldDays.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            for (Iterator<CalendarDate> iter = newDays.listIterator(); iter.hasNext(); ) {
                CalendarDate date = iter.next();
                CalendarDate date0 = (CalendarDate) pair.getKey();
                if (date0.equals(date)) {
                    it.remove();
                    iter.remove();
                }
            }
        }

        for (Map.Entry<CalendarDate, Integer> dateOld : oldDays.entrySet()) {
            db.delete(TABLE_ROUTINE_DAYS, ROUTINE_DAYS_DAY_ID + " = ?", new String[]{String.valueOf(dateOld.getValue())});
        }

        for (CalendarDate date : newDays) {

            ContentValues dayValues = new ContentValues();
            dayValues.put(ROUTINE_DAYS_ROUTINE_ID, routine.getId());
            dayValues.put(ROUTINE_DAYS_DATE, date.toString());
            dayValues.put(ROUTINE_DAYS_IS_DONE, 0);

            db.insert(TABLE_ROUTINE_DAYS, null, dayValues);
        }

        ContentValues values = new ContentValues();
        values.put(ROUTINE_ID, routine.getId());
        values.put(ROUTINE_NAME, routine.getName());
        values.put(ROUTINE_COLOR, routine.getColor());
        values.put(ROUTINE_START_DATE, routine.getStartDate().toString());
        values.put(ROUTINE_END_DATE, routine.getEndDate().toString());
        values.put(ROUTINE_NOTE, routine.getNote());

        return db.update(TABLE_ROUTINE, values, ROUTINE_ID + " = ?",
                new String[]{String.valueOf(routine.getId())});
    }

    public Integer deleteRoutine(long id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ROUTINE_DAYS, ROUTINE_DAYS_ROUTINE_ID + " = ? ", new String[]{String.valueOf(id)});

        db.delete(TABLE_ROUTINE_WEEKDAYS, ROUTINE_WEEKDAYS_ROUTINE_ID + " = ? ", new String[]{String.valueOf(id)});

        return db.delete(TABLE_ROUTINE, ROUTINE_ID + " = ? ", new String[]{String.valueOf(id)});
    }

    public Routine getRoutine(long id) {

        Routine routine = new Routine();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_ROUTINE + " WHERE "
                + ROUTINE_ID + " = " + id;

        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
            routine.setId(c.getInt((c.getColumnIndex(ROUTINE_ID))));
            routine.setName(c.getString(c.getColumnIndex(ROUTINE_NAME)));
            routine.setColor(c.getInt(c.getColumnIndex(ROUTINE_COLOR)));
            routine.setStartDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_START_DATE))));
            routine.setEndDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_END_DATE))));
            routine.setNote(c.getString(c.getColumnIndex(ROUTINE_NOTE)));

            List<WeekDay> weekDays = new ArrayList<>();

            String subQuery = "SELECT " + ROUTINE_WEEKDAYS_WEEKDAY_ID + " FROM "
                    + TABLE_ROUTINE_WEEKDAYS + " WHERE " + ROUTINE_WEEKDAYS_ROUTINE_ID + " = " + routine.getId();

            Cursor cursor = db.rawQuery(subQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    weekDays.add(WeekDay.get(cursor.getInt((cursor.getColumnIndex(ROUTINE_WEEKDAYS_WEEKDAY_ID)))));
                } while (cursor.moveToNext());
            }

            cursor.close();

            routine.setWeekdays(weekDays);

            c.close();
        }

        return routine;
    }

    public List<Routine> getMonthRoutines(CalendarDate start, CalendarDate finish) {

        List<Routine> routines = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + ROUTINE_ID + ", " + ROUTINE_NAME + ", " + ROUTINE_COLOR
                + ", " + ROUTINE_START_DATE + ", " + ROUTINE_END_DATE + ", "
                + ROUTINE_NOTE + " FROM " + TABLE_ROUTINE + " WHERE "
                + ROUTINE_END_DATE + " >= '" + start.toString() + "' AND "
                + ROUTINE_START_DATE + " <= '" + finish.toString() + "' ORDER BY " + ROUTINE_ID;

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                Routine routine = new Routine();
                routine.setId(c.getInt((c.getColumnIndex(ROUTINE_ID))));
                routine.setName(c.getString(c.getColumnIndex(ROUTINE_NAME)));
                routine.setColor(c.getInt((c.getColumnIndex(ROUTINE_COLOR))));
                routine.setStartDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_START_DATE))));
                routine.setEndDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_END_DATE))));
                routine.setNote(c.getString(c.getColumnIndex(ROUTINE_NOTE)));

                List<WeekDay> weekDays = new ArrayList<>();
                String subQuery = "SELECT " + ROUTINE_WEEKDAYS_WEEKDAY_ID +
                        " FROM " + TABLE_ROUTINE_WEEKDAYS
                        + " WHERE " + ROUTINE_WEEKDAYS_ROUTINE_ID + " = " + routine.getId();

                Cursor cursor = db.rawQuery(subQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        weekDays.add(WeekDay.get(cursor.getInt((cursor.getColumnIndex(ROUTINE_WEEKDAYS_WEEKDAY_ID)))));
                    } while (cursor.moveToNext());
                }

                cursor.close();

                routine.setWeekdays(weekDays);
                routines.add(routine);
            } while (c.moveToNext());
        }

        c.close();

        return routines;
    }


    public List<RoutineDayCircle> getRoutineMonthCircles(CalendarDate start, CalendarDate finish) {

        List<Routine> routineList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT "
                + ROUTINE_ID + ", "
                + ROUTINE_COLOR
                + " FROM " + TABLE_ROUTINE + " WHERE "
                + ROUTINE_END_DATE + " >= '" + start.toString() + "' AND "
                + ROUTINE_START_DATE + " <= '" + finish.toString() + "' ORDER BY " + ROUTINE_ID + " LIMIT 15";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                Routine routine = new Routine();
                routine.setId(c.getInt((c.getColumnIndex(ROUTINE_ID))));
                routine.setColor(c.getInt((c.getColumnIndex(ROUTINE_COLOR))));
                List<RoutineDate> dates = new ArrayList<>();

                String subQuery = "SELECT "
                        + ROUTINE_DAYS_DATE + ", "
                        + ROUTINE_DAYS_IS_DONE
                        + " FROM " + TABLE_ROUTINE_DAYS + " WHERE "
                        + ROUTINE_DAYS_DATE + " BETWEEN '" + start.toString() + "' AND '" + finish.toString() + "' AND "
                        + ROUTINE_DAYS_ROUTINE_ID + " = " + routine.getId();

                Cursor cursor = db.rawQuery(subQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        RoutineDate date = new RoutineDate();
                        date.setDate(CalendarDate.getDate(cursor.getString(cursor.getColumnIndex(ROUTINE_DAYS_DATE))));
                        boolean done = (cursor.getInt(cursor.getColumnIndex(ROUTINE_DAYS_IS_DONE)) == 1);
                        date.setDone(done);
                        dates.add(date);
                    } while (cursor.moveToNext());
                }

                cursor.close();

                routine.setDays(dates);
                routineList.add(routine);
            } while (c.moveToNext());
        }

        c.close();

        List<RoutineDayCircle> circles = new ArrayList<>();

        for (int i = 0; i < routineList.size(); i++) {
            Routine routine = routineList.get(i);
            for (RoutineDate day : routine.getDays()) {
                RoutineDayCircle circle = new RoutineDayCircle(i, day.getDate(), routine.getColor(), day.isDone());
                circles.add(circle);
            }
        }

        return circles;
    }

    public List<RoutineDay> getUncompletedRoutineDays(CalendarDate date) {

        List<RoutineDay> routineDays = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + ROUTINE_ID + ", " + ROUTINE_NAME + ", " + ROUTINE_COLOR + ", " + ROUTINE_START_DATE
                + ", " + ROUTINE_END_DATE + ", " + ROUTINE_NOTE + ", " + ROUTINE_DAYS_DAY_ID + ", "
                + ROUTINE_DAYS_DATE + ", " + ROUTINE_DAYS_IS_DONE
                + " FROM " + TABLE_ROUTINE_DAYS
                + " LEFT OUTER JOIN " + TABLE_ROUTINE
                + " ON " + TABLE_ROUTINE_DAYS + "." + ROUTINE_DAYS_ROUTINE_ID + " = " + TABLE_ROUTINE + "." + ROUTINE_ID
                + " WHERE " + ROUTINE_DAYS_DATE + " = '" + date.toString() + "' AND " + ROUTINE_DAYS_IS_DONE + " = 0";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                RoutineDay routineDay = new RoutineDay();
                Routine routine = new Routine();
                routine.setId(c.getInt((c.getColumnIndex(ROUTINE_ID))));
                routine.setName(c.getString(c.getColumnIndex(ROUTINE_NAME)));
                routine.setColor(c.getInt((c.getColumnIndex(ROUTINE_COLOR))));
                routine.setStartDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_START_DATE))));
                routine.setEndDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_END_DATE))));
                routine.setNote(c.getString(c.getColumnIndex(ROUTINE_NOTE)));

                List<WeekDay> weekDays = new ArrayList<>();
                String subQuery = "SELECT " + ROUTINE_WEEKDAYS_WEEKDAY_ID + " FROM "
                        + TABLE_ROUTINE_WEEKDAYS + " WHERE " + ROUTINE_WEEKDAYS_ROUTINE_ID + " = " + routine.getId();

                Cursor cursor = db.rawQuery(subQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        weekDays.add(WeekDay.get(cursor.getInt((cursor.getColumnIndex(ROUTINE_WEEKDAYS_WEEKDAY_ID)))));
                    } while (cursor.moveToNext());
                }

                cursor.close();

                routine.setWeekdays(weekDays);

                routineDay.setId(c.getInt((c.getColumnIndex(ROUTINE_DAYS_DAY_ID))));
                routineDay.setRoutine(routine);
                routineDay.setDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_DAYS_DATE))));
                boolean done = (c.getInt(c.getColumnIndex(ROUTINE_DAYS_IS_DONE)) == 1);
                routineDay.setDone(done);
                routineDays.add(routineDay);
            } while (c.moveToNext());
        }

        c.close();

        return routineDays;
    }

    public List<RoutineDay> getAllRoutineDays(CalendarDate date) {

        List<RoutineDay> routineDays = new ArrayList<>();

        routineDays.addAll(getUncompletedRoutineDays(date));

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + ROUTINE_ID + ", " + ROUTINE_NAME + ", " + ROUTINE_COLOR + ", " + ROUTINE_START_DATE
                + ", " + ROUTINE_END_DATE + ", " + ROUTINE_NOTE + ", " + ROUTINE_DAYS_DAY_ID + ", "
                + ROUTINE_DAYS_DATE + ", " + ROUTINE_DAYS_IS_DONE
                + " FROM " + TABLE_ROUTINE_DAYS
                + " LEFT OUTER JOIN " + TABLE_ROUTINE
                + " ON " + TABLE_ROUTINE_DAYS + "." + ROUTINE_DAYS_ROUTINE_ID + " = " + TABLE_ROUTINE + "." + ROUTINE_ID
                + " WHERE " + ROUTINE_DAYS_DATE + " = '" + date.toString() + "' AND " + ROUTINE_DAYS_IS_DONE + " = 1";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                RoutineDay routineDay = new RoutineDay();
                Routine routine = new Routine();
                routine.setId(c.getInt((c.getColumnIndex(ROUTINE_ID))));
                routine.setName(c.getString(c.getColumnIndex(ROUTINE_NAME)));
                routine.setColor(c.getInt((c.getColumnIndex(ROUTINE_COLOR))));
                routine.setStartDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_START_DATE))));
                routine.setEndDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_END_DATE))));
                routine.setNote(c.getString(c.getColumnIndex(ROUTINE_NOTE)));

                List<WeekDay> weekDays = new ArrayList<>();
                String subQuery = "SELECT " + ROUTINE_WEEKDAYS_WEEKDAY_ID + " FROM "
                        + TABLE_ROUTINE_WEEKDAYS + " WHERE " + ROUTINE_WEEKDAYS_ROUTINE_ID + " = " + routine.getId();

                Cursor cursor = db.rawQuery(subQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        weekDays.add(WeekDay.get(cursor.getInt((cursor.getColumnIndex(ROUTINE_WEEKDAYS_WEEKDAY_ID)))));
                    } while (cursor.moveToNext());
                }

                cursor.close();

                routine.setWeekdays(weekDays);

                routineDay.setId(c.getInt((c.getColumnIndex(ROUTINE_DAYS_DAY_ID))));
                routineDay.setRoutine(routine);
                routineDay.setDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_DAYS_DATE))));
                boolean done = (c.getInt(c.getColumnIndex(ROUTINE_DAYS_IS_DONE)) == 1);
                routineDay.setDone(done);
                routineDays.add(routineDay);
            } while (c.moveToNext());
        }

        c.close();

        return routineDays;
    }


    public RoutineDay getRoutineDay(int id) {

        RoutineDay routineDay = new RoutineDay();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + ROUTINE_ID + ", " + ROUTINE_NAME + ", " + ROUTINE_COLOR + ", " + ROUTINE_START_DATE
                + ", " + ROUTINE_END_DATE + ", " + ROUTINE_NOTE + ", " + ROUTINE_DAYS_DAY_ID + ", "
                + ROUTINE_DAYS_DATE + ", " + ROUTINE_DAYS_IS_DONE
                + " FROM " + TABLE_ROUTINE_DAYS
                + " LEFT OUTER JOIN " + TABLE_ROUTINE
                + " ON " + TABLE_ROUTINE_DAYS + "." + ROUTINE_DAYS_ROUTINE_ID + " = " + TABLE_ROUTINE + "." + ROUTINE_ID
                + " WHERE " + ROUTINE_DAYS_DAY_ID + " = " + id;

        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();

            Routine routine = new Routine();
            routine.setId(c.getInt((c.getColumnIndex(ROUTINE_ID))));
            routine.setName(c.getString(c.getColumnIndex(ROUTINE_NAME)));
            routine.setColor(c.getInt((c.getColumnIndex(ROUTINE_COLOR))));
            routine.setStartDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_START_DATE))));
            routine.setEndDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_END_DATE))));
            routine.setNote(c.getString(c.getColumnIndex(ROUTINE_NOTE)));

            List<WeekDay> weekDays = new ArrayList<>();
            String subQuery = "SELECT " + ROUTINE_WEEKDAYS_WEEKDAY_ID + " FROM "
                    + TABLE_ROUTINE_WEEKDAYS + " WHERE " + ROUTINE_WEEKDAYS_ROUTINE_ID + " = " + routine.getId();

            Cursor cursor = db.rawQuery(subQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    weekDays.add(WeekDay.get(cursor.getInt((cursor.getColumnIndex(ROUTINE_WEEKDAYS_WEEKDAY_ID)))));
                } while (cursor.moveToNext());
            }

            cursor.close();

            routine.setWeekdays(weekDays);

            routineDay.setId(c.getInt((c.getColumnIndex(ROUTINE_DAYS_DAY_ID))));
            routineDay.setRoutine(routine);
            routineDay.setDate(CalendarDate.getDate(c.getString(c.getColumnIndex(ROUTINE_DAYS_DATE))));
            boolean done = (c.getInt(c.getColumnIndex(ROUTINE_DAYS_IS_DONE)) == 1);
            routineDay.setDone(done);

            c.close();
        }



        return routineDay;
    }

    public int updateRoutineDay(RoutineDay routineDay) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ROUTINE_DAYS_DAY_ID, routineDay.getId());
        values.put(ROUTINE_DAYS_ROUTINE_ID, routineDay.getRoutine().getId());
        values.put(ROUTINE_DAYS_DATE, routineDay.getDate().toString());
        values.put(ROUTINE_DAYS_IS_DONE, routineDay.isDone());

        return db.update(TABLE_ROUTINE_DAYS, values, ROUTINE_DAYS_DAY_ID + " = ?",
                new String[]{String.valueOf(routineDay.getId())});
    }

}
