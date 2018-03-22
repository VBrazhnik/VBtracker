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

import com.vb.tracker.R;
import com.vb.tracker.free.colorpicker.ColorListener;
import com.vb.tracker.free.colorpicker.ColorPalette;
import com.vb.tracker.free.colorpicker.ColorPicker;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.datepicker.DateListener;
import com.vb.tracker.free.datepicker.DatePickerDialog;
import com.vb.tracker.free.todocalendar.ToDo;

public class ToDoTab extends Fragment {

    private ToDo todo = new ToDo();
    private View view;

    private View toastLayout;
    private TextView toastText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_todo_tab, container, false);

        final Button pickDate = view.findViewById(R.id.pick_date);
        ImageView colorPicker = view.findViewById(R.id.color_picker);

        toastLayout = inflater.inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.custom_toast_layout));
        toastText = toastLayout.findViewById(R.id.custom_toast_message);

        final Drawable colorPickerDrawable = colorPicker.getDrawable();
        DrawableCompat.setTint(colorPickerDrawable, ColorPalette.AMBER.getColor());
        todo.setColor(ColorPalette.AMBER.getColor());

        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ColorPicker fragment = new ColorPicker();

                ColorListener listener = new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        DrawableCompat.setTint(colorPickerDrawable, color);
                        todo.setColor(color);
                        fragment.dismiss();
                    }
                };

                fragment.setColorListener(listener);
                fragment.show(getFragmentManager(), "Dialog");
            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final DatePickerDialog fragment = new DatePickerDialog();

                DateListener listener = new DateListener() {
                    @Override
                    public void onDateClicked(CalendarDate date) {
                        pickDate.setText(date.toFormattedString());
                        todo.setDate(date);
                        fragment.dismiss();
                    }
                };

                fragment.setCalendarListener(listener);
                fragment.show(getFragmentManager(), "Date Picker");

            }
        });

        return view;
    }

    public ToDo getTodo() {

        TextView nameText = view.findViewById(R.id.input_todo_name);
        todo.setName(nameText.getText().toString());

        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(nameText.getWindowToken(), 0);
        }

        TextView noteText = view.findViewById(R.id.note);
        todo.setNote(noteText.getText().toString());

        Toast toast = Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toastText.setTextColor(ColorPalette.RED.getColor());

        if (todo.getName().equals("") ){
            toastText.setText(R.string.name_is_not_entered);
            toast.show();
            return null;
        } else if(todo.getDate() == null){
            toastText.setText(R.string.date_is_not_selected);
            toast.show();
            return null;
        }

        return todo;
    }
}
