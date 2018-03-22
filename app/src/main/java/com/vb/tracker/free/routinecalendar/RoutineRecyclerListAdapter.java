package com.vb.tracker.free.routinecalendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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

class RoutineRecyclerListAdapter extends RecyclerView.Adapter<RoutineRecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<String> items = new ArrayList<>();
    private final List<Integer> color = new ArrayList<>();
    private List<RoutineDay> routineDays = new ArrayList<>();

    private Context context;
    private CalendarDate date;
    private RoutineCalendar calendar;

    private RoutineRecyclerListFragment routineRecyclerListFragment;

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(CalendarDate date) {
        DatabaseHelper db = new DatabaseHelper(context);

        routineDays = db.getAllRoutineDays(date);

        items.clear();
        color.clear();

        for (RoutineDay routineDay : routineDays) {
            items.add(routineDay.getRoutine().getName());
            color.add(routineDay.getRoutine().getColor());
        }

        ImageView done = ((Activity) context).findViewById(R.id.done);
        if (routineDays.isEmpty()) {
            done.setVisibility(View.VISIBLE);
        } else {
            done.setVisibility(View.GONE);
        }

        this.date = date;
    }

    RoutineRecyclerListAdapter(RoutineRecyclerListFragment toDoRecyclerListFragment, RoutineCalendar calendar) {
        this.routineRecyclerListFragment = toDoRecyclerListFragment;
        this.context = toDoRecyclerListFragment.getContext();
        this.calendar = calendar;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        switch (viewType) {
            case 0:
                itemViewHolder = new ItemViewHolderNotDone(view);
                break;
            case 1:
                itemViewHolder = new ItemViewHolderDone(view);
                break;
        }

        return itemViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return (!routineDays.get(position).isDone()) ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.textView.setText(items.get(position));

        if (holder instanceof ItemViewHolderNotDone) {
            holder.textView.setTextColor(color.get(position));
        } else if (holder instanceof ItemViewHolderDone) {
            final TypedValue value = new TypedValue ();
            context.getTheme().resolveAttribute (R.attr.colorAccent, value, true);
            holder.textView.setTextColor(value.data);
            holder.textView.setAlpha(0.5f);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RoutineDialog alert = new RoutineDialog(routineRecyclerListFragment);

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar.updateCalendarView();
                        setDate(date);
                        notifyDataSetChanged();
                    }
                };

                alert.setListener(listener);
                alert.showDialog(routineDays.get(position).getRoutine());
            }
        });

    }

    @Override
    public void onItemRightDismiss(int position) {
        DatabaseHelper db = new DatabaseHelper(context);
        RoutineDay routineDay = routineDays.get(position);
        routineDay.setDone(true);

        db.updateRoutineDay(routineDay);

        items.remove(position);
        calendar.updateCalendarView();
        setDate(date);

        notifyDataSetChanged();
    }

    @Override
    public void onItemLeftDismiss(int position) {
        DatabaseHelper db = new DatabaseHelper(context);
        RoutineDay routineDay = routineDays.get(position);
        routineDay.setDone(false);

        db.updateRoutineDay(routineDay);

        items.remove(position);
        calendar.updateCalendarView();
        setDate(date);

        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class ItemViewHolderDone extends ItemViewHolder {

        ItemViewHolderDone(View itemView) {
            super(itemView);
        }

    }

    static class ItemViewHolderNotDone extends ItemViewHolder {

        ItemViewHolderNotDone(View itemView) {
            super(itemView);
        }

    }

    static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        final TextView textView;

        ItemViewHolder(View itemView) {
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