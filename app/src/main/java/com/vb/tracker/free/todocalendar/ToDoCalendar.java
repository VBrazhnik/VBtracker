package com.vb.tracker.free.todocalendar;

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

import com.vb.tracker.R;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.datepicker.DateListener;
import com.vb.tracker.free.helper.OnSwipeTouchListener;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ToDoCalendar extends LinearLayout {

    Context context;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    ImageView previousMonthButton, nextMonthButton;

    Calendar calendar;

    TextView dateText;

    int cellSize;

    CoordinatorLayout monthLayout;
    LinearLayout weekdayLayout, dayLayout;

    static DateListener clickDate;

    public ToDoCalendar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ToDoCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        inflate(getContext(), R.layout.todo_calendar, this);

        monthLayout = (CoordinatorLayout) findViewById(R.id.todo_calendar_month);
        weekdayLayout = (LinearLayout) findViewById(R.id.todo_calendar_weekday);
        dayLayout = (LinearLayout) findViewById(R.id.todo_calendar_cell_view_layout);

        calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        recyclerView = (RecyclerView) findViewById(R.id.todo_calendar_cell_view);

        this.layoutManager = new GridLayoutManager(getContext(), 7, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        setMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        previousMonthButton = (ImageView) findViewById(R.id.todo_calendar_month_back);
        nextMonthButton = (ImageView) findViewById(R.id.todo_calendar_month_forward);

        dateText = (TextView) findViewById(R.id.todo_calendar_month_year);

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
        this.adapter = new ToDoCalenderAdapter(context, dayMax, firstDay, month, year, cellSize);
        this.recyclerView.setAdapter(adapter);
    }

    public static DateListener staticClickInterface() {

        if (clickDate != null) {
            return clickDate;
        } else {
            return null;
        }
    }

    public void setOnClickDate(DateListener clickDate) {

        ToDoCalendar.clickDate = clickDate;

    }

    public void setFullScreenWidth() {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        cellSize = (width) / 7;
        setMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        TextView mon = (TextView) findViewById(R.id.mon);
        mon.getLayoutParams().width = cellSize;

        TextView tue = (TextView) findViewById(R.id.tue);
        tue.getLayoutParams().width = cellSize;

        TextView wed = (TextView) findViewById(R.id.wed);
        wed.getLayoutParams().width = cellSize;

        TextView thu = (TextView) findViewById(R.id.thu);
        thu.getLayoutParams().width = cellSize;

        TextView fri = (TextView) findViewById(R.id.fri);
        fri.getLayoutParams().width = cellSize;

        TextView sat = (TextView) findViewById(R.id.sat);
        sat.getLayoutParams().width = cellSize;

        TextView sun = (TextView) findViewById(R.id.sun);
        sun.getLayoutParams().width = cellSize;

    }


    void updateCalendarView() {
        setMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }
}
