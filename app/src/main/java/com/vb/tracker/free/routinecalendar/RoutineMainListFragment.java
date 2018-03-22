package com.vb.tracker.free.routinecalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vb.tracker.free.datepicker.CalendarDate;

public class RoutineMainListFragment extends Fragment {

    private CalendarDate date;
    private RoutineCalendar calendar;

    public RoutineMainListFragment() {
    }

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(CalendarDate date) {
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(getContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RoutineMainListAdapter adapter = new RoutineMainListAdapter(RoutineMainListFragment.this, calendar);

        if (date != null) {
            adapter.setDate(date);
        }

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new RoutineSimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public RoutineCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(RoutineCalendar calendar) {
        this.calendar = calendar;
    }
}
