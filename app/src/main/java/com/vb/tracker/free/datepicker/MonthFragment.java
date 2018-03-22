package com.vb.tracker.free.datepicker;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.vb.tracker.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MonthFragment extends Fragment {

    private RecyclerView recyclerView;

    private Calendar calendar;

    private static DateListener clickDate;
    private ViewGroup view;

    private int month;
    private int year;

    private int cellWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.date_picker_month, container, false);

        calendar = new GregorianCalendar(year, month, 1);

        recyclerView = view.findViewById(R.id.date_picker_month_cell_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 7);
        recyclerView.setLayoutManager(layoutManager);

        setMonth(month, year);

        setFullScreenWidth();
        return view;
    }

    public void setMonthYear(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public void setMonth(int month, int year) {

        int dayMax = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = calendar.get(Calendar.DAY_OF_WEEK) + 6;
        RecyclerView.Adapter adapter = new MonthFragmentAdapter(dayMax, firstDay, month, year, cellWidth);
        this.recyclerView.setAdapter(adapter);

    }

    public static DateListener staticClickInterface() {
        if (clickDate != null)
            return clickDate;
        else
            return null;
    }

    public void setOnClickDate(DateListener clickDate) {
        MonthFragment.clickDate = clickDate;
    }

    public void setFullScreenWidth() {

        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        cellWidth = (width) / 7;

        setMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        TextView mon = view.findViewById(R.id.mon);
        mon.getLayoutParams().width = cellWidth;

        TextView tue = view.findViewById(R.id.tue);
        tue.getLayoutParams().width = cellWidth;

        TextView wed = view.findViewById(R.id.wed);
        wed.getLayoutParams().width = cellWidth;

        TextView thu = view.findViewById(R.id.thu);
        thu.getLayoutParams().width = cellWidth;

        TextView fri = view.findViewById(R.id.fri);
        fri.getLayoutParams().width = cellWidth;

        TextView sat = view.findViewById(R.id.sat);
        sat.getLayoutParams().width = cellWidth;

        TextView sun = view.findViewById(R.id.sun);
        sun.getLayoutParams().width = cellWidth;

    }
}
