package com.vb.tracker.free.routinecalendar;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vb.tracker.R;
import com.vb.tracker.free.datepicker.CalendarDate;

import java.util.List;

class RoutineCalenderAdapter extends RecyclerView.Adapter<RoutineCalenderAdapter.ViewHolder> {

    private Context context;
    private int maxDay, firstDay, month, year;

    private int cellSize = 51;

    private List<RoutineDayCircle> badges;

    RoutineCalenderAdapter(Context context, int maxDay, int firstDay, int month, int year, int cellSize, List<RoutineDayCircle> badges) {

        this.context = context;
        this.maxDay = maxDay;
        this.firstDay = firstDay;
        this.month = month;
        this.year = year;
        this.cellSize = cellSize;
        this.badges = badges;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_calendar_cell, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (position > (firstDay - 2) && position < (maxDay + firstDay - 1)) {

            int toDay = (position - (firstDay - 2));

            holder.date.setText(String.valueOf(toDay));

            for (int i = 0; i < badges.size(); i++) {
                if (toDay == badges.get(i).getDate().getDay()) {

                    switch (badges.get(i).getPosition()) {
                        case 0:
                            holder.circle0.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle0.setImageAlpha(50);
                            holder.circle0.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 1:
                            holder.circle1.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle1.setImageAlpha(50);
                            holder.circle1.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 2:
                            holder.circle2.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle2.setImageAlpha(50);
                            holder.circle2.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 3:
                            holder.circle3.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle3.setImageAlpha(50);
                            holder.circle3.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 4:
                            holder.circle4.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle4.setImageAlpha(50);
                            holder.circle4.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 5:
                            holder.circle5.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle5.setImageAlpha(50);
                            holder.circle5.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 6:
                            holder.circle6.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle6.setImageAlpha(50);
                            holder.circle6.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 7:
                            holder.circle7.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle7.setImageAlpha(50);
                            holder.circle7.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 8:
                            holder.circle8.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle8.setImageAlpha(50);
                            holder.circle8.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 9:
                            holder.circle9.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle9.setImageAlpha(50);
                            holder.circle9.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 10:
                            holder.circle10.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle10.setImageAlpha(50);
                            holder.circle10.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 11:
                            holder.circle11.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle11.setImageAlpha(50);
                            holder.circle11.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 12:
                            holder.circle12.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle12.setImageAlpha(50);
                            holder.circle12.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 13:
                            holder.circle13.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle13.setImageAlpha(50);
                            holder.circle13.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 14:
                            holder.circle14.setVisibility(View.VISIBLE);
                            if (!badges.get(i).isDone()) holder.circle14.setImageAlpha(50);
                            holder.circle14.setColorFilter(badges.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
                            break;
                    }

                }
            }

        } else {
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

                    if (RoutineCalendar.staticClickInterface() != null)
                        RoutineCalendar.staticClickInterface().onDateClicked(date);
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

        TextView date;
        ImageView circle0;
        ImageView circle1;
        ImageView circle2;
        ImageView circle3;
        ImageView circle4;
        ImageView circle5;
        ImageView circle6;
        ImageView circle7;
        ImageView circle8;
        ImageView circle9;
        ImageView circle10;
        ImageView circle11;
        ImageView circle12;
        ImageView circle13;
        ImageView circle14;


        FrameLayout cell;

        public ViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.routine_calendar_cell_date);

            cell = view.findViewById(R.id.routine_calendar_cell);

            circle0 = view.findViewById(R.id.routine_calendar_cell_circle_0);
            circle1 = view.findViewById(R.id.routine_calendar_cell_circle_1);
            circle2 = view.findViewById(R.id.routine_calendar_cell_circle_2);
            circle3 = view.findViewById(R.id.routine_calendar_cell_circle_3);
            circle4 = view.findViewById(R.id.routine_calendar_cell_circle_4);
            circle5 = view.findViewById(R.id.routine_calendar_cell_circle_5);
            circle6 = view.findViewById(R.id.routine_calendar_cell_circle_6);
            circle7 = view.findViewById(R.id.routine_calendar_cell_circle_7);
            circle8 = view.findViewById(R.id.routine_calendar_cell_circle_8);
            circle9 = view.findViewById(R.id.routine_calendar_cell_circle_9);
            circle10 = view.findViewById(R.id.routine_calendar_cell_circle_10);
            circle11 = view.findViewById(R.id.routine_calendar_cell_circle_11);
            circle12 = view.findViewById(R.id.routine_calendar_cell_circle_12);
            circle13 = view.findViewById(R.id.routine_calendar_cell_circle_13);
            circle14 = view.findViewById(R.id.routine_calendar_cell_circle_14);

            cell.setLayoutParams(new LinearLayout.LayoutParams(cellSize, cellSize));

        }


    }

}

