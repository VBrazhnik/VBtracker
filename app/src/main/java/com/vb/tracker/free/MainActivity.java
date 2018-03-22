package com.vb.tracker.free;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;

import com.vb.tracker.R;
import com.vb.tracker.free.datepicker.CalendarDate;
import com.vb.tracker.free.lock.BaseActivity;
import com.vb.tracker.free.parts.AgendaFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CalendarDate.months= new String[] {
                getString(R.string.january),
                getString(R.string.february),
                getString(R.string.march),
                getString(R.string.april),
                getString(R.string.may),
                getString(R.string.june),
                getString(R.string.july),
                getString(R.string.august),
                getString(R.string.september),
                getString(R.string.october),
                getString(R.string.november),
                getString(R.string.december)};

        updateTheme();

        setContentView(R.layout.activity);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.launcher_icon);

        final TypedValue value = new TypedValue ();
        getTheme().resolveAttribute (R.attr.background, value, true);

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getString(R.string.app_name), bm, value.data);
        this.setTaskDescription(taskDescription);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new AgendaFragment());
        transaction.commit();

    }

    public void updateTheme() {
        if (Utility.getTheme(getApplication()) <= Utility.THEME_BLACK_WHITE) {
            setTheme(R.style.AppTheme_BlackWhite);
        } else if (Utility.getTheme(getApplication()) == Utility.THEME_WHITE_BLACK) {
            setTheme(R.style.AppTheme_WhiteBlack);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
