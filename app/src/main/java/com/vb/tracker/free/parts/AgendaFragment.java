package com.vb.tracker.free.parts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vb.tracker.R;
import com.vb.tracker.free.SettingFragment;
import com.vb.tracker.free.add.AddFragment;
import com.vb.tracker.free.agenda.RecyclerListFragment;

public class AgendaFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.agenda_content, container, false);

        RecyclerListFragment fragment = new RecyclerListFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.agenda_content, fragment)
                .commit();

        ImageView headerIcon = getActivity().findViewById(R.id.header_icon);
        headerIcon.setImageResource(R.drawable.icon_agenda);

        TextView headerTitle = getActivity().findViewById(R.id.header_title);
        headerTitle.setText(R.string.agenda);

        ImageButton setting = getActivity().findViewById(R.id.header_menu_icon);
        setting.setVisibility(View.VISIBLE);
        setting.setImageResource(R.drawable.menu_icon_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SettingFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddFragment();
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

        ImageButton agenda = getActivity().findViewById(R.id.footer_agenda);
        agenda.setOnClickListener(null);

        return view;
    }

}





