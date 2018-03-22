package com.vb.tracker.free.add;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vb.tracker.R;
import com.vb.tracker.free.DatabaseHelper;
import com.vb.tracker.free.parts.AgendaFragment;
import com.vb.tracker.free.parts.RoutineFragment;
import com.vb.tracker.free.parts.ToDoFragment;
import com.vb.tracker.free.routinecalendar.Routine;
import com.vb.tracker.free.todocalendar.ToDo;

public class AddFragment extends Fragment {

    private int position;
    private PagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_fragment, container, false);

        ImageView headerIcon = getActivity().findViewById(R.id.header_icon);
        headerIcon.setImageResource(R.drawable.icon_add_new);

        TextView headerTitle = getActivity().findViewById(R.id.header_title);
        headerTitle.setText(R.string.create_new);

        ImageButton save = getActivity().findViewById(R.id.header_menu_icon);
        save.setImageResource(R.drawable.menu_icon_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (save()) {
                    Fragment fragment = new AgendaFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, fragment);
                    transaction.commit();
                }
            }
        });

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.icon_todo));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.icon_routine));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.pager);
        adapter = new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        ImageButton agenda = getActivity().findViewById(R.id.footer_agenda);
        agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AgendaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        ImageButton todo = getActivity().findViewById(R.id.footer_todo);
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ToDoFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        ImageButton routine = getActivity().findViewById(R.id.footer_routine);
        routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RoutineFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        return view;
    }

    public boolean save() {

        if (position == 0) {

            ToDoTab tab = (ToDoTab) adapter.getItem(position);
            ToDo toDo = tab.getTodo();

            if (toDo != null) {
                DatabaseHelper db = new DatabaseHelper(getContext());
                db.createToDo(toDo);
                return true;
            }

        } else if (position == 1) {

            RoutineTab tab = (RoutineTab) adapter.getItem(position);
            Routine routine = tab.getRoutine();

            if (routine != null) {
                DatabaseHelper db = new DatabaseHelper(getContext());
                db.createRoutine(routine);
                return true;
            }
        }

        return false;
    }
}
