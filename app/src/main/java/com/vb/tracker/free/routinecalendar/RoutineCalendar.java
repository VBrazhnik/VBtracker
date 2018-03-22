package com.vb.tracker.free.routinecalendar;

import android.content.Context;
import android.graphics.Point;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vb.tracker.free.DatabaseHelper;
import com.vb.tracker.R;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.datepicker.DateListener;
import com.vb.tracker.free.helper.OnSwipeTouchListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RoutineCalendar extends LinearLayout {

    Context context;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    ImageView previousMonthButton, nextMonthButton;

    Calendar calendar;

    TextView dateText;

    int cellSize = 51;

    CoordinatorLayout monthLayout;
    LinearLayout weekdayLayout, dayLayout;

    static DateListener clickDate;

    public RoutineCalendar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public RoutineCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        inflate(getContext(), R.layout.routine_calendar, this);

        monthLayout = findViewById(R.id.routine_calendar_month);
        weekdayLayout = findViewById(R.id.routine_calendar_weekday);
        dayLayout = findViewById(R.id.routine_calendar_cell_view_layout);

        calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        recyclerView = findViewById(R.id.routine_calendar_cell_view);

        this.layoutManager = new GridLayoutManager(getContext(), 7, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        setMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        previousMonthButton = findViewById(R.id.routine_calendar_month_back);
        nextMonthButton = findViewById(R.id.routine_calendar_month_forward);

        dateText = findViewById(R.id.routine_calendar_month_year);

        dateText.setText(CalendarDate.getFormattedMonthAndYear(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));

        previousMonthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPreviousMonth();
            }
        });

        nextMonthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNextMonth();
            }
        });

        OnSwipeTouchListener listen = new OnSwipeTouchListener(this.getContext()) {

            public void onSwipeLeft() {
                selectNextMonth();
            }

            public void onSwipeRight() {
                selectPreviousMonth();
            }
        };

        monthLayout.setOnTouchListener(listen);
        weekdayLayout.setOnTouchListener(listen);

        setFullScreenWidth();
    }

    private void selectNextMonth() {

        calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) + 1));
        int month = calendar.get(Calendar.MONTH);
        dateText.setText(CalendarDate.getFormattedMonthAndYear(month, calendar.get(Calendar.YEAR)));
        setMonth(month, calendar.get(Calendar.YEAR));

    }

    private void selectPreviousMonth() {

        calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) - 1));
        int month = calendar.get(Calendar.MONTH);
        dateText.setText(CalendarDate.getFormattedMonthAndYear(month, calendar.get(Calendar.YEAR)));
        setMonth(month, calendar.get(Calendar.YEAR));

    }

    public void setMonth(int month, int year) {

        int dayMax = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = calendar.get(Calendar.DAY_OF_WEEK) + 6;
        this.adapter = new RoutineCalenderAdapter(context, dayMax, firstDay, month, year, cellSize, badgesMonth(month, year));
        this.recyclerView.setAdapter(adapter);

    }

    public static DateListener staticClickInterface() {

        if (clickDate != null) {
            return clickDate;
        }else {
            return null;
        }
    }

    public void setOnClickDate(DateListener clickDate) {
        RoutineCalendar.clickDate = clickDate;
    }


    private List<RoutineDayCircle> badgesMonth(int month, int year) {
        DatabaseHelper db = new DatabaseHelper(context);
        return db.getRoutineMonthCircles(new CalendarDate(1, month, year), new CalendarDate(CalendarDate.getMaxDayOfMonth(month, year), month, year));
    }

    public void setFullScreenWidth() {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        Point size = new Point();
        if (display != null) {
            display.getSize(size);
        }
        int width = size.x;

        cellSize = (width) / 7;
        setMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        TextView mon = findViewById(R.id.mon);
        mon.getLayoutParams().width = cellSize;

        TextView tue = findViewById(R.id.tue);
        tue.getLayoutParams().width = cellSize;

        TextView wed = findViewById(R.id.wed);
        wed.getLayoutParams().width = cellSize;

        TextView thu = findViewById(R.id.thu);
        thu.getLayoutParams().width = cellSize;

        TextView fri = findViewById(R.id.fri);
        fri.getLayoutParams().width = cellSize;

        TextView sat = findViewById(R.id.sat);
        sat.getLayoutParams().width = cellSize;

        TextView sun = findViewById(R.id.sun);
        sun.getLayoutParams().width = cellSize;
    }

    void updateCalendarView() {
        setMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }

    public CalendarDate getCurrentDate() {
        return new CalendarDate(1, calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }
}
