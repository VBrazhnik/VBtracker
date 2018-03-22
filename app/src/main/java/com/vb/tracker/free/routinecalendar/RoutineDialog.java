package com.vb.tracker.free.routinecalendar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.vb.tracker.free.DatabaseHelper;
import com.vb.tracker.R;
import com.vb.tracker.free.Utility;
import com.vb.tracker.free.colorpicker.ColorListener;
import com.vb.tracker.free.colorpicker.ColorPalette;
import com.vb.tracker.free.colorpicker.ColorPicker;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.datepicker.DateListener;
import com.vb.tracker.free.datepicker.DatePickerDialog;

public class RoutineDialog extends Dialog {

    private View.OnClickListener listener;

    private Fragment routineRecyclerListFragment;
    private boolean isEditable = false;

    private TextView startDateText;
    private TextView endDateText;

    private ViewSwitcher nameSwitcher;
    private ViewSwitcher noteSwitcher;

    private EditText editText;

    private EditText editName;

    private Routine routine;

    private ImageButton trash;

    private ImageView colorPicker;

    private ToggleButton mon;
    private ToggleButton tue;
    private ToggleButton wed;
    private ToggleButton thu;
    private ToggleButton fri;
    private ToggleButton sat;
    private ToggleButton sun;

    private View toastLayout;
    private TextView toastText;

    public RoutineDialog(Fragment routineRecyclerListFragment) {
        super(routineRecyclerListFragment.getContext());
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setCancelable(true);
        super.setContentView(R.layout.routine_dialog);
        this.routineRecyclerListFragment = routineRecyclerListFragment;
    }


    public void showDialog(final Routine originalRoutine) {

        Utility.updateTheme(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());

        toastLayout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        toastText = toastLayout.findViewById(R.id.custom_toast_message);

        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
        sun = findViewById(R.id.sun);

        editText = findViewById(R.id.info_note_edit);

        editName = findViewById(R.id.input_todo_name);

        this.routine = originalRoutine;

        startDateText = findViewById(R.id.info_start_date);
        endDateText = findViewById(R.id.info_end_date);

        nameSwitcher = findViewById(R.id.switcher2);
        noteSwitcher = findViewById(R.id.switcher);

        colorPicker = findViewById(R.id.color_picker);

        ImageButton cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        trash = findViewById(R.id.trash);
        trash.setImageDrawable(ContextCompat.getDrawable(routineRecyclerListFragment.getContext(), R.drawable.dialog_icon_trash));

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(routineRecyclerListFragment.getContext());
                db.deleteRoutine((int) routine.getId());
                if (listener != null) listener.onClick(v);
                dismiss();
            }
        });

        final ImageButton edit = findViewById(R.id.edit);
        edit.setImageDrawable(ContextCompat.getDrawable(routineRecyclerListFragment.getContext(), R.drawable.dialog_icon_edit));

        see();

        View.OnClickListener listener0 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditable) {
                    edit.setImageDrawable(ContextCompat.getDrawable(routineRecyclerListFragment.getContext(), R.drawable.dialog_icon_save));
                    nameSwitcher.showNext();
                    noteSwitcher.showNext();
                    edit();
                } else {

                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

                    routine.getWeekdays().clear();
                    if (mon.isChecked()) routine.getWeekdays().add(WeekDay.MONDAY);
                    if (tue.isChecked()) routine.getWeekdays().add(WeekDay.TUESDAY);
                    if (wed.isChecked()) routine.getWeekdays().add(WeekDay.WEDNESDAY);
                    if (thu.isChecked()) routine.getWeekdays().add(WeekDay.THURSDAY);
                    if (fri.isChecked()) routine.getWeekdays().add(WeekDay.FRIDAY);
                    if (sat.isChecked()) routine.getWeekdays().add(WeekDay.SATURDAY);
                    if (sun.isChecked()) routine.getWeekdays().add(WeekDay.SUNDAY);

                    routine.setName(editName.getText().toString());
                    routine.setNote(editText.getText().toString());

                    Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toastText.setTextColor(ColorPalette.RED.getColor());

                    if (routine.getName().equals("")){
                        toastText.setText(R.string.name_is_not_entered);
                        toast.show();
                    }else if(routine.getStartDate() == null){
                        toastText.setText(R.string.start_date_is_not_selected);
                        toast.show();
                    }else if(routine.getEndDate() == null){
                        toastText.setText(R.string.end_date_is_not_selected);
                        toast.show();
                    }
                    else if(routine.getEndDate().toString().compareTo(routine.getStartDate().toString()) <= 0){
                        toastText.setText(R.string.end_date_should_go_after_start_date);
                        toast.show();
                    }else if(routine.getWeekdays().isEmpty()){
                        toastText.setText(R.string.weekdays_is_not_selected);
                        toast.show();
                    }
                    else{
                    DatabaseHelper db = new DatabaseHelper(routineRecyclerListFragment.getContext());
                    db.updateRoutine(routine);

                    edit.setImageDrawable(ContextCompat.getDrawable(routineRecyclerListFragment.getContext(), R.drawable.dialog_icon_edit));
                    nameSwitcher.showNext();
                    noteSwitcher.showNext();
                    see();
                    if (listener != null) listener.onClick(v);
                    }
                }

            }
        };

        edit.setOnClickListener(listener0);

        super.show();

    }

    private void edit() {

        mon.setEnabled(true);
        tue.setEnabled(true);
        wed.setEnabled(true);
        thu.setEnabled(true);
        fri.setEnabled(true);
        sat.setEnabled(true);
        sun.setEnabled(true);

        for (WeekDay weekday : routine.getWeekdays()) {
            switch (weekday.getDayNumber()) {
                case 1:
                    mon.setChecked(true);
                    break;
                case 2:
                    tue.setChecked(true);
                    break;
                case 3:
                    wed.setChecked(true);
                    break;
                case 4:
                    thu.setChecked(true);
                    break;
                case 5:
                    fri.setChecked(true);
                    break;
                case 6:
                    sat.setChecked(true);
                    break;
                case 7:
                    sun.setChecked(true);
                    break;
            }
        }

        noteSwitcher.setVisibility(View.VISIBLE);

        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatePickerDialog fragment = new DatePickerDialog();

                DateListener listener = new DateListener() {
                    @Override
                    public void onDateClicked(CalendarDate date) {
                        startDateText.setText(date.toFormattedString());
                        routine.setStartDate(date);
                        fragment.dismiss();
                    }
                };

                fragment.setCalendarListener(listener);
                FragmentManager fm = routineRecyclerListFragment.getFragmentManager();
                fragment.show(fm, "Date Picker");

            }
        });

        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatePickerDialog fragment = new DatePickerDialog();

                DateListener listener = new DateListener() {
                    @Override
                    public void onDateClicked(CalendarDate date) {
                        endDateText.setText(date.toFormattedString());
                        routine.setEndDate(date);
                        fragment.dismiss();
                    }
                };

                fragment.setCalendarListener(listener);
                FragmentManager fm = routineRecyclerListFragment.getFragmentManager();
                fragment.show(fm, "Date Picker");

            }
        });


        final Drawable colorPickerDrawable = colorPicker.getDrawable();
        DrawableCompat.setTint(colorPickerDrawable, routine.getColor());

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
                fragment.show(routineRecyclerListFragment.getFragmentManager(), "Dialog");
            }
        });

        editText.setText(routine.getNote());

        editName.setText(routine.getName());

        isEditable = true;
    }


    private void see() {
        DatabaseHelper db = new DatabaseHelper(routineRecyclerListFragment.getContext());
        routine = db.getRoutine((int) routine.getId());

        mon.setEnabled(false);
        tue.setEnabled(false);
        wed.setEnabled(false);
        thu.setEnabled(false);
        fri.setEnabled(false);
        sat.setEnabled(false);
        sun.setEnabled(false);

        for (WeekDay weekday : routine.getWeekdays()) {
            switch (weekday.getDayNumber()) {
                case 1:
                    mon.setChecked(true);
                    break;
                case 2:
                    tue.setChecked(true);
                    break;
                case 3:
                    wed.setChecked(true);
                    break;
                case 4:
                    thu.setChecked(true);
                    break;
                case 5:
                    fri.setChecked(true);
                    break;
                case 6:
                    sat.setChecked(true);
                    break;
                case 7:
                    sun.setChecked(true);
                    break;
            }
        }


        TextView name = findViewById(R.id.info_name);
        TextView note = findViewById(R.id.info_note);


        name.setText(routine.getName());
        name.setTextColor(routine.getColor());
        name.setMovementMethod(new ScrollingMovementMethod());

        startDateText.setText(routine.getStartDate().toFormattedString());
        startDateText.setOnClickListener(null);

        endDateText.setText(routine.getEndDate().toFormattedString());
        endDateText.setOnClickListener(null);

        colorPicker.setOnClickListener(null);

        if (routine.getNote().equals("")) {
            noteSwitcher.setVisibility(View.GONE);
        } else {
            note.setText(routine.getNote());
            noteSwitcher.setVisibility(View.VISIBLE);
        }

        note.setMovementMethod(new ScrollingMovementMethod());
        isEditable = false;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
