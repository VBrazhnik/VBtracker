package com.vb.tracker.free.routinecalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vb.tracker.R;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.datepicker.DateListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class RoutineCalendarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final View v = inflater.inflate(R.layout.routine_fragment, container, false);

        final RoutineCalendar calendar = v.findViewById(R.id.routine_calendar_view);

        final TextView monthYearText = v.findViewById(R.id.routine_calendar_month_year);

        final SlidingUpPanelLayout sliding = v.findViewById(R.id.routine_sliding_layout);

        final RoutineRecyclerListFragment fragment = new RoutineRecyclerListFragment();
        fragment.setCalendar(calendar);
        getFragmentManager().beginTransaction()
                .add(R.id.routine_day_routines, fragment)
                .commit();
        fragment.setDate(CalendarDate.getCurrentDate());
        final TextView date_name = v.findViewById(R.id.routine_calendar_day_date);
        date_name.setText(CalendarDate.getCurrentDate().toFormattedString());

        calendar.setOnClickDate(new DateListener() {
            @Override
            public void onDateClicked(CalendarDate date) {

                final RoutineRecyclerListFragment fragment = new RoutineRecyclerListFragment();
                fragment.setCalendar(calendar);
                getFragmentManager().beginTransaction()
                        .replace(R.id.routine_day_routines, fragment)
                        .commit();
                fragment.setDate(date);
                date_name.setText(date.toFormattedString());
                sliding.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        monthYearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RoutineMainListFragment fragment = new RoutineMainListFragment();
                fragment.setCalendar(calendar);
                getFragmentManager().beginTransaction()
                        .replace(R.id.routine_day_routines, fragment)
                        .commit();
                fragment.setDate(calendar.getCurrentDate());
                date_name.setText(CalendarDate.getFormattedMonthAndYear(calendar.getCurrentDate().getMonth(), calendar.getCurrentDate().getYear()));
                sliding.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        return v;
    }

}
