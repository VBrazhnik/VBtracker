package com.vb.tracker.free.add;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vb.tracker.R;
import com.vb.tracker.free.colorpicker.ColorListener;
import com.vb.tracker.free.colorpicker.ColorPalette;
import com.vb.tracker.free.colorpicker.ColorPicker;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.datepicker.DateListener;
import com.vb.tracker.free.datepicker.DatePickerDialog;
import com.vb.tracker.free.routinecalendar.Routine;
import com.vb.tracker.free.routinecalendar.WeekDay;

import java.util.ArrayList;
import java.util.List;

public class RoutineTab extends Fragment {

    private ToggleButton mon;
    private ToggleButton tue;
    private ToggleButton wed;
    private ToggleButton thu;
    private ToggleButton fri;
    private ToggleButton sat;
    private ToggleButton sun;

    private Routine routine = new Routine();

    private View view;

    private View toastLayout;
    private TextView toastText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_routine_tab, container, false);

        toastLayout = inflater.inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.custom_toast_layout));
        toastText = toastLayout.findViewById(R.id.custom_toast_message);

        mon = view.findViewById(R.id.mon);
        tue = view.findViewById(R.id.tue);
        wed = view.findViewById(R.id.wed);
        thu = view.findViewById(R.id.thu);
        fri = view.findViewById(R.id.fri);
        sat = view.findViewById(R.id.sat);
        sun = view.findViewById(R.id.sun);

        wed.setChecked(true);
        fri.setChecked(true);

        final Button pickStartDate = view.findViewById(R.id.pick_routine_start_date);
        final Button pickEndDate = view.findViewById(R.id.pick_routine_end_date);

        ImageView colorPicker = view.findViewById(R.id.color_picker_routine);

        final Drawable colorPickerDrawable = colorPicker.getDrawable();
        DrawableCompat.setTint(colorPickerDrawable, ColorPalette.AMBER.getColor());
        routine.setColor(ColorPalette.AMBER.getColor());

        colorPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final ColorPicker fragment = new ColorPicker();

                ColorListener listener = new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        DrawableCompat.setTint(colorPickerDrawable, color);
                        routine.setColor(color);
                        fragment.dismiss();
                    }
                };

                fragment.setColorListener(listener);
                fragment.show(getFragmentManager(), "Dialog");
            }
        });

        pickStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final DatePickerDialog fragment = new DatePickerDialog();

                DateListener listener = new DateListener() {
                    @Override
                    public void onDateClicked(CalendarDate date) {
                        pickStartDate.setText(date.toFormattedString());
                        routine.setStartDate(date);
                        fragment.dismiss();
                    }
                };

                fragment.setCalendarListener(listener);
                fragment.show(getFragmentManager(), "Date Picker");
            }
        });

        pickEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final DatePickerDialog fragment = new DatePickerDialog();

                DateListener listener = new DateListener() {
                    @Override
                    public void onDateClicked(CalendarDate date) {
                        pickEndDate.setText(date.toFormattedString());
                        routine.setEndDate(date);
                        fragment.dismiss();
                    }
                };

                fragment.setCalendarListener(listener);
                fragment.show(getFragmentManager(), "Date Picker");
            }
        });

        return view;
    }

    public Routine getRoutine() {

        TextView nameText = view.findViewById(R.id.input_routine_name);
        routine.setName(nameText.getText().toString());

        TextView noteText = view.findViewById(R.id.routine_note);
        routine.setNote(noteText.getText().toString());

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(nameText.getWindowToken(), 0);
        }

        List<WeekDay> weekdays = new ArrayList<>();

        if (mon.isChecked()) weekdays.add(WeekDay.MONDAY);
        if (tue.isChecked()) weekdays.add(WeekDay.TUESDAY);
        if (wed.isChecked()) weekdays.add(WeekDay.WEDNESDAY);
        if (thu.isChecked()) weekdays.add(WeekDay.THURSDAY);
        if (fri.isChecked()) weekdays.add(WeekDay.FRIDAY);
        if (sat.isChecked()) weekdays.add(WeekDay.SATURDAY);
        if (sun.isChecked()) weekdays.add(WeekDay.SUNDAY);

        routine.setWeekdays(weekdays);

        Toast toast = Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toastText.setTextColor(ColorPalette.RED.getColor());

        if (routine.getName().equals("")) {
            toastText.setText(R.string.name_is_not_entered);
            toast.show();
            return null;
        } else if (routine.getStartDate() == null) {
            toastText.setText(R.string.start_date_is_not_selected);
            toast.show();
            return null;
        } else if (routine.getEndDate() == null) {
            toastText.setText(R.string.end_date_is_not_selected);
            toast.show();
            return null;
        } else if (routine.getEndDate().toString().compareTo(routine.getStartDate().toString()) <= 0) {
            toastText.setText(R.string.end_date_should_go_after_start_date);
            toast.show();
            return null;
        } else if (routine.getWeekdays().isEmpty()) {
            toastText.setText(R.string.weekdays_is_not_selected);
            toast.show();
            return null;
        }

        return routine;
    }
}
