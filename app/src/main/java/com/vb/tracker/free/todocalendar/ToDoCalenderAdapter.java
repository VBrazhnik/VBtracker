package com.vb.tracker.free.todocalendar;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vb.tracker.free.DatabaseHelper;
import com.vb.tracker.R;
import com.vb.tracker.free.colorpicker.ColorPalette;
import com.vb.tracker.free.datepicker.CalendarDate;

import java.util.List;

class ToDoCalenderAdapter extends RecyclerView.Adapter<ToDoCalenderAdapter.ViewHolder> {

    private Context context;
    private int maxDay, firstDay, month, year;
    private int cellSize;
    private List<ToDoBadge> badges;

    ToDoCalenderAdapter(Context context, int maxDay, int firstDay, int month, int year, int cellSize) {

        this.context = context;
        this.maxDay = maxDay;
        this.firstDay = firstDay;
        this.month = month;
        this.year = year;
        this.cellSize = cellSize;

        DatabaseHelper db = new DatabaseHelper(context);
        this.badges = db.getBadges(new CalendarDate(1, month, year), new CalendarDate(CalendarDate.getMaxDayOfMonth(month, year), month, year));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_calendar_cell, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.badge.setVisibility(View.GONE);

        if (position > (firstDay - 2) && position < (maxDay + firstDay - 1)) {

            int toDay = (position - (firstDay - 2));

            holder.date.setText(String.valueOf(toDay));

            for (int i = 0; i < badges.size(); i++) {

                if (toDay == badges.get(i).getDate().getDay()) {

                    int color = 0;

                    holder.badge.setText("+" + badges.get(i).getCount());

                    if (0 == badges.get(i).getCount()) {
                        holder.badge.setAlpha(0.5f);
                        holder.badge.setText("");
                        holder.badge.setBackground(ContextCompat.getDrawable(context, R.drawable.todo_circle_done));
                        final TypedValue value = new TypedValue ();
                        context.getTheme().resolveAttribute (R.attr.colorAccent, value, true);
                        color = value.data;
                    } else if (3 >= badges.get(i).getCount() && badges.get(i).getCount() >= 1) {
                        color = ColorPalette.GREEN.getColor();
                    } else if (6 >= badges.get(i).getCount() && badges.get(i).getCount() >= 4) {
                        color = ColorPalette.ORANGE.getColor();
                    } else if (6 <= badges.get(i).getCount() && badges.get(i).getCount() <= 99) {
                        color = ColorPalette.RED.getColor();
                    }
                    else if (99 < badges.get(i).getCount()) {
                        holder.badge.setText(R.string.overflow);
                        color = ColorPalette.RED.getColor();
                    }

                    Drawable badgeDrawable = holder.badge.getBackground();
                    badgeDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

                    holder.badge.setTextColor(color);
                    holder.badge.setVisibility(View.VISIBLE);
                }
            }

        } else {
            holder.cell.setVisibility(View.VISIBLE);

            final TypedValue value = new TypedValue ();
            context.getTheme().resolveAttribute (R.attr.colorPrimaryDark, value, true);

            Drawable e = ResourcesCompat.getDrawable(context.getResources(), R.drawable.calendar_rectangle_crossed, null);

            holder.cell.setBackground(e);

            holder.cell.getBackground().setColorFilter(value.data, PorterDuff.Mode.SRC_ATOP);

            holder.date.setText("");
        }

        holder.cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > (firstDay - 2) && position < (firstDay + maxDay - 1)) {
                    int toDay = (position - (firstDay - 2));
                    CalendarDate date = new CalendarDate(toDay, month, year);
                    if (ToDoCalendar.staticClickInterface() != null)
                        ToDoCalendar.staticClickInterface().onDateClicked(date);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (firstDay > 7) {
            firstDay = firstDay - 7;
        }
        return 42;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView date;
        TextView badge;
        FrameLayout cell;

        public ViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.todo_calendar_cell_date);
            badge = (TextView) view.findViewById(R.id.todo_calendar_cell_badge);
            cell = (FrameLayout) view.findViewById(R.id.todo_calendar_cell);

            cell.setLayoutParams(new LinearLayout.LayoutParams(cellSize, cellSize));

        }
    }

}

