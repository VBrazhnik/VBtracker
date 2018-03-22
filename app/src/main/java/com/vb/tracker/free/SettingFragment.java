package com.vb.tracker.free;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vb.tracker.R;
import com.vb.tracker.free.colorpicker.ColorPalette;
import com.vb.tracker.free.lock.AppLock;
import com.vb.tracker.free.lock.AppLockActivity;
import com.vb.tracker.free.lock.LockManager;
import com.vb.tracker.free.parts.AgendaFragment;
import com.vb.tracker.free.parts.RoutineFragment;
import com.vb.tracker.free.parts.ToDoFragment;

public class SettingFragment extends Fragment {

    private View view;
    private Button btOnOff;
    private Button btChange;

    private View toastLayout;
    private TextView toastText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.setting_fragment, container, false);

        toastLayout = inflater.inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.custom_toast_layout));
        toastText = toastLayout.findViewById(R.id.custom_toast_message);

        ImageView headerIcon = getActivity().findViewById(R.id.header_icon);
        headerIcon.setImageResource(R.drawable.icon_setting);

        TextView headerTitle = getActivity().findViewById(R.id.header_title);
        headerTitle.setText(R.string.setting);

        ImageButton setting = getActivity().findViewById(R.id.header_menu_icon);
        setting.setVisibility(View.INVISIBLE);
        setting.setImageResource(R.drawable.menu_icon_setting);
        setting.setOnClickListener(null);

        FrameLayout theme_button = view.findViewById(R.id.setting_item_theme);

        ImageView theme = view.findViewById(R.id.icon_theme);
        TextView theme_text = view.findViewById(R.id.text_theme);

        if (Utility.getTheme(getActivity()) <= Utility.THEME_BLACK_WHITE) {
            theme.setImageResource(R.drawable.theme_mode_night);
            theme_text.setText(R.string.set_night_mode);
        } else if (Utility.getTheme(getActivity()) == Utility.THEME_WHITE_BLACK) {
            theme.setImageResource(R.drawable.theme_mode_day);
            theme_text.setText(R.string.set_day_mode);
        }

        theme_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.getTheme(getActivity()) <= Utility.THEME_BLACK_WHITE) {
                    Utility.setTheme(getActivity(), 2);
                } else if (Utility.getTheme(getActivity()) == Utility.THEME_WHITE_BLACK) {
                    Utility.setTheme(getActivity(), 1);
                }

                recreateActivity();
            }
        });

        btOnOff = view.findViewById(R.id.password_enable_disable);
        btOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = LockManager.getInstance().getAppLock().isPasswordSet() ? AppLock.DISABLE_PASSWORD
                        : AppLock.ENABLE_PASSWORD;
                Intent intent = new Intent(getActivity(), AppLockActivity.class);
                intent.putExtra(AppLock.TYPE, type);
                startActivityForResult(intent, type);
            }
        });

        btChange = view.findViewById(R.id.password_change);
        btChange.setText(R.string.change_password);
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppLockActivity.class);
                intent.putExtra(AppLock.TYPE, AppLock.CHANGE_PASSWORD);
                intent.putExtra(AppLock.MESSAGE,
                        getString(R.string.enter_current_password));
                startActivityForResult(intent, AppLock.CHANGE_PASSWORD);
            }
        });

        updateUI();

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppLock.DISABLE_PASSWORD:
                break;
            case AppLock.ENABLE_PASSWORD:
            case AppLock.CHANGE_PASSWORD:
                if (resultCode == AppLockActivity.RESULT_OK) {

                    Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toastText.setText(getString(R.string.password_enabled));
                    toastText.setTextColor(ColorPalette.GREEN.getColor());
                    toast.setView(toastLayout);
                    toast.show();

                }
                break;
            default:
                break;
        }
        updateUI();
    }


    private void updateUI() {
        if (LockManager.getInstance().getAppLock().isPasswordSet()) {
            btOnOff.setText(R.string.disable_password);
            btChange.setAlpha(1);
            btChange.setEnabled(true);
        } else {
            btOnOff.setText(R.string.enable_password);
            btChange.setAlpha(0.5f);

            btChange.setEnabled(false);
        }
    }

    public void recreateActivity() {

        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Activity activity = getActivity();
        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
        activity.finish();
        getActivity().overridePendingTransition(0, 0);
    }
}
