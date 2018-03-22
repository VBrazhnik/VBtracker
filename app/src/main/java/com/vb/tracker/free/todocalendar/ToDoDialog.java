package com.vb.tracker.free.todocalendar;

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

public class ToDoDialog extends Dialog {

    private View.OnClickListener listener;

    private Fragment toDoRecyclerListFragment;
    private boolean isEditable = false;

    private TextView dateText;

    private ViewSwitcher nameSwitcher;
    private ViewSwitcher noteSwitcher;

    private EditText editText;

    private EditText editName;

    private ToDo todo;

    private ImageButton trash;

    private ImageView colorPicker;

    private View toastLayout;
    private TextView toastText;

    public ToDoDialog(Fragment toDoRecyclerListFragment) {
        super(toDoRecyclerListFragment.getContext());
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setCancelable(true);
        super.setContentView(R.layout.todo_dialog);
        this.toDoRecyclerListFragment = toDoRecyclerListFragment;
    }

    public void showDialog(final ToDo originalTodo) {

        Utility.updateTheme(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());

        toastLayout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        toastText = (TextView) toastLayout.findViewById(R.id.custom_toast_message);

        editText = (EditText) findViewById(R.id.info_note_edit);

        editName = (EditText) findViewById(R.id.input_todo_name);

        this.todo = originalTodo;

        dateText = (TextView) findViewById(R.id.info_date);

        nameSwitcher = (ViewSwitcher) findViewById(R.id.switcher2);
        noteSwitcher = (ViewSwitcher) findViewById(R.id.switcher);

        colorPicker = (ImageView) findViewById(R.id.color_picker);

        ImageButton cancel = (ImageButton) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        trash = (ImageButton) findViewById(R.id.trash);
        trash.setImageDrawable(ContextCompat.getDrawable(toDoRecyclerListFragment.getContext(), R.drawable.dialog_icon_trash));

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(toDoRecyclerListFragment.getContext());
                db.deleteToDo((int) todo.getId());
                if (listener != null) listener.onClick(v);
                dismiss();
            }
        });

        final ImageButton edit = (ImageButton) findViewById(R.id.edit);
        edit.setImageDrawable(ContextCompat.getDrawable(toDoRecyclerListFragment.getContext(), R.drawable.dialog_icon_edit));

        see();

        View.OnClickListener listener0 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditable) {
                    edit.setImageDrawable(ContextCompat.getDrawable(toDoRecyclerListFragment.getContext(), R.drawable.dialog_icon_save));
                    nameSwitcher.showNext();
                    noteSwitcher.showNext();
                    edit();
                } else {

                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

                    todo.setName(editName.getText().toString());
                    todo.setNote(editText.getText().toString());

                    Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toastText.setTextColor(ColorPalette.RED.getColor());

                    if (todo.getName().equals("") ){
                        toastText.setText(R.string.name_is_not_entered);
                        toast.show();
                    }else if(todo.getDate() == null){
                        toastText.setText(R.string.date_is_not_selected);
                        toast.show();
                    }else{
                    DatabaseHelper db = new DatabaseHelper(toDoRecyclerListFragment.getContext());
                    db.updateToDo(todo);

                    edit.setImageDrawable(ContextCompat.getDrawable(toDoRecyclerListFragment.getContext(), R.drawable.dialog_icon_edit));

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

        noteSwitcher.setVisibility(View.VISIBLE);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatePickerDialog fragment = new DatePickerDialog();

                DateListener listener = new DateListener() {
                    @Override
                    public void onDateClicked(CalendarDate date) {
                        dateText.setText(date.toFormattedString());
                        todo.setDate(date);
                        fragment.dismiss();
                    }
                };

                fragment.setCalendarListener(listener);
                FragmentManager fm = toDoRecyclerListFragment.getFragmentManager();
                fragment.show(fm, "Date Picker");

            }
        });


        final Drawable colorPickerDrawable = colorPicker.getDrawable();
        DrawableCompat.setTint(colorPickerDrawable, todo.getColor());

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
                fragment.show(toDoRecyclerListFragment.getFragmentManager(), "Dialog");
            }
        });

        editText.setText(todo.getNote());

        editName.setText(todo.getName());

        isEditable = true;
    }


    private void see() {

        TextView name = (TextView) findViewById(R.id.info_name);
        TextView note = (TextView) findViewById(R.id.info_note);

        DatabaseHelper db = new DatabaseHelper(toDoRecyclerListFragment.getContext());
        todo = db.getToDo((int) todo.getId());

        name.setText(todo.getName());
        name.setTextColor(todo.getColor());
        name.setMovementMethod(new ScrollingMovementMethod());

        dateText.setText(todo.getDate().toFormattedString());
        dateText.setOnClickListener(null);

        colorPicker.setOnClickListener(null);

        if (todo.getNote().equals("")) {
            noteSwitcher.setVisibility(View.GONE);
        } else {
            note.setText(todo.getNote());
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
