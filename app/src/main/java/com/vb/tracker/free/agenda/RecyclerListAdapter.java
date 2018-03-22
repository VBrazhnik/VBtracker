package com.vb.tracker.free.agenda;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vb.tracker.R;
import com.vb.tracker.free.DatabaseHelper;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.helper.ItemTouchHelperAdapter;
import com.vb.tracker.free.helper.ItemTouchHelperViewHolder;
import com.vb.tracker.free.routinecalendar.RoutineDay;
import com.vb.tracker.free.routinecalendar.RoutineDialog;
import com.vb.tracker.free.todocalendar.ToDo;
import com.vb.tracker.free.todocalendar.ToDoDialog;

import java.util.ArrayList;
import java.util.List;

class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<String> items = new ArrayList<>();
    private final List<Integer> colors = new ArrayList<>();
    private List<Sample> samples = new ArrayList<>();

    private Fragment fragment;

    private Context context;

    RecyclerListAdapter(RecyclerListFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        setDate();
    }

    public void setDate() {
        DatabaseHelper db = new DatabaseHelper(context);

        samples.clear();
        items.clear();
        colors.clear();

        for (ToDo todo : db.getUncompletedToDos(CalendarDate.getCurrentDate())) {
            samples.add(new Sample((int) todo.getId(), 1));
            items.add(todo.getName());
            colors.add(todo.getColor());
        }

        for (RoutineDay routine : db.getUncompletedRoutineDays(CalendarDate.getCurrentDate())) {
            samples.add(new Sample((int) routine.getId(), 2));
            items.add(routine.getRoutine().getName());
            colors.add(routine.getRoutine().getColor());
        }

        if (samples.isEmpty()) {
            ImageView done = fragment.getActivity().findViewById(R.id.done);
            done.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final DatabaseHelper db = new DatabaseHelper(context);
        holder.textView.setText(items.get(position));
        holder.textView.setTextColor(colors.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (samples.get(position).getType() == 1) {
                    final ToDoDialog alert = new ToDoDialog(fragment);

                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setDate();
                            notifyDataSetChanged();
                        }
                    };

                    alert.setListener(listener);
                    alert.showDialog(db.getToDo(samples.get(position).getId()));
                } else if (samples.get(position).getType() == 2) {
                    final RoutineDialog alert = new RoutineDialog(fragment);

                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setDate();
                            notifyDataSetChanged();
                        }
                    };

                    alert.setListener(listener);
                    alert.showDialog(db.getRoutineDay(samples.get(position).getId()).getRoutine());
                }
            }
        });

    }

    @Override
    public void onItemRightDismiss(int position) {
        DatabaseHelper db = new DatabaseHelper(context);

        if (samples.get(position).getType() == 1) {
            ToDo toDo = db.getToDo(samples.get(position).getId());
            toDo.setDone(true);
            db.updateToDo(toDo);
        } else if (samples.get(position).getType() == 2) {
            RoutineDay routineDay = db.getRoutineDay(samples.get(position).getId());
            routineDay.setDone(true);
            db.updateRoutineDay(routineDay);
        }

        items.remove(position);
        setDate();
        notifyItemRemoved(position);
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
