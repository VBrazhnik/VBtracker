package com.vb.tracker.free.todocalendar;

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

class ToDoRecyclerListAdapter extends RecyclerView.Adapter<ToDoRecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<String> items = new ArrayList<>();
    private final List<Integer> color = new ArrayList<>();
    private List<ToDo> todos = new ArrayList<>();

    private Context context;
    private CalendarDate date;
    private ToDoCalendar calendar;

    private ToDoRecyclerListFragment toDoRecyclerListFragment;

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(CalendarDate date) {
        DatabaseHelper db = new DatabaseHelper(context);

        todos = db.getToDos(date);

        items.clear();
        color.clear();

        for (ToDo todo : todos) {
            items.add(todo.getName());
            color.add(todo.getColor());
        }
        ImageView done = (ImageView) ((Activity) context).findViewById(R.id.done);
        if (todos.isEmpty()) {
            done.setVisibility(View.VISIBLE);
        } else {
            done.setVisibility(View.GONE);
        }

        this.date = date;
    }

    ToDoRecyclerListAdapter(ToDoRecyclerListFragment toDoRecyclerListFragment, ToDoCalendar calendar) {
        this.toDoRecyclerListFragment = toDoRecyclerListFragment;
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
        return (!todos.get(position).isDone()) ? 0 : 1;
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
                final ToDoDialog alert = new ToDoDialog(toDoRecyclerListFragment);

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar.updateCalendarView();
                        setDate(date);
                        notifyDataSetChanged();
                    }
                };

                alert.setListener(listener);

                alert.showDialog(todos.get(position));

            }
        });

    }

    @Override
    public void onItemRightDismiss(int position) {
        DatabaseHelper db = new DatabaseHelper(context);
        ToDo toDo = todos.get(position);
        toDo.setDone(true);

        db.updateToDo(toDo);

        items.remove(position);
        calendar.updateCalendarView();
        setDate(date);

        notifyDataSetChanged();
    }

    @Override
    public void onItemLeftDismiss(int position) {
        DatabaseHelper db = new DatabaseHelper(context);
        ToDo toDo = todos.get(position);
        toDo.setDone(false);

        db.updateToDo(toDo);

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
            textView = (TextView) itemView.findViewById(R.id.text);
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