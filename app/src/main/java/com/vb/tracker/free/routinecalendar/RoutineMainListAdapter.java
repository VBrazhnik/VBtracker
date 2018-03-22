package com.vb.tracker.free.routinecalendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vb.tracker.free.DatabaseHelper;
import com.vb.tracker.R;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.helper.*;

import java.util.ArrayList;
import java.util.List;

class RoutineMainListAdapter extends RecyclerView.Adapter<RoutineMainListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<String> items = new ArrayList<>();
    private final List<Integer> color = new ArrayList<>();
    private List<Routine> routines = new ArrayList<>();

    private Context context;
    private CalendarDate date;
    private RoutineCalendar calendar;

    private RoutineMainListFragment mainListFragment;

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(CalendarDate date) {
        DatabaseHelper db = new DatabaseHelper(context);

        routines = db.getMonthRoutines(new CalendarDate(1, date.getMonth(), date.getYear()), new CalendarDate(CalendarDate.getMaxDayOfMonth(date.getMonth(), date.getYear()), date.getMonth(), date.getYear()));

        items.clear();
        color.clear();

        for (Routine routineDay : routines) {
            items.add(routineDay.getName());
            color.add(routineDay.getColor());
        }

        ImageView done = ((Activity) context).findViewById(R.id.done);
        if (routines.isEmpty()) {

            done.setVisibility(View.VISIBLE);

        } else {
            done.setVisibility(View.GONE);
        }

        this.date = date;
    }

    public RoutineMainListAdapter(RoutineMainListFragment mainListFragment, RoutineCalendar calendar) {
        this.mainListFragment = mainListFragment;
        this.context = mainListFragment.getContext();
        this.calendar = calendar;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.textView.setText(items.get(position));
        holder.textView.setTextColor(color.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RoutineDialog alert = new RoutineDialog(mainListFragment);

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar.updateCalendarView();
                        setDate(date);
                        notifyDataSetChanged();
                    }
                };

                alert.setListener(listener);

                alert.showDialog(routines.get(position));
            }
        });

    }

    @Override
    public void onItemRightDismiss(int position) {
    }

    @Override
    public void onItemLeftDismiss(int position) {
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }


}