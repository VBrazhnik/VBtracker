package com.vb.tracker.free.todocalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vb.tracker.R;
import com.vb.tracker.free.datepicker.*;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class ToDoCalendarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final View v = inflater.inflate(R.layout.todo_fragment, container, false);

        final ToDoCalendar calendar = (ToDoCalendar) v.findViewById(R.id.todo_calendar_view);

        final ToDoRecyclerListFragment fragment = new ToDoRecyclerListFragment();
        fragment.setCalendar(calendar);
        getFragmentManager().beginTransaction()
                .add(R.id.todo_day_todos, fragment)
                .commit();

        fragment.setDate(CalendarDate.getCurrentDate());
        final TextView dateText = (TextView) v.findViewById(R.id.todo_calendar_day_date);
        dateText.setText(CalendarDate.getCurrentDate().toFormattedString());

        final SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) v.findViewById(R.id.todo_sliding_layout);

        calendar.setOnClickDate(new DateListener() {
            @Override
            public void onDateClicked(CalendarDate date) {
                final ToDoRecyclerListFragment fragment = new ToDoRecyclerListFragment();
                fragment.setCalendar(calendar);
                getFragmentManager().beginTransaction()
                        .replace(R.id.todo_day_todos, fragment)
                        .commit();
                fragment.setDate(date);
                dateText.setText(date.toFormattedString());
                sliding.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

            }
        });

        return v;
    }
}
