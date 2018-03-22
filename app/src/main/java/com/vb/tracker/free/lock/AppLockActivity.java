package com.vb.tracker.free.lock;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vb.tracker.R;
import com.vb.tracker.free.Utility;
import com.vb.tracker.free.VBtracker;
import com.vb.tracker.free.colorpicker.ColorPalette;

public class AppLockActivity extends BaseActivity {

    private int type = -1;
    private String oldPassword = null;

    protected EditText passwordCircle1;
    protected EditText passwordCircle2;
    protected EditText passwordCircle3;
    protected EditText passwordCircle4;

    protected InputFilter[] filters;
    protected TextView passwordMessage;

    View toastLayout;
    TextView toastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateTheme();

        setContentView(R.layout.password_activity);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.launcher_icon);

        final TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.background, value, true);

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(VBtracker.getInstance().getString(R.string.app_name), bm, value.data);
        this.setTaskDescription(taskDescription);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        toastLayout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        toastText = toastLayout.findViewById(R.id.custom_toast_message);

        passwordMessage = findViewById(R.id.password_message);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String message = extras.getString(AppLock.MESSAGE);
            if (message != null) {
                passwordMessage.setText(message);
            }

            type = extras.getInt(AppLock.TYPE, -1);
        }

        filters = new InputFilter[2];
        filters[0] = new InputFilter.LengthFilter(1);
        filters[1] = numberFilter;

        passwordCircle1 = findViewById(R.id.password_circle_1);
        setupEditText(passwordCircle1);

        passwordCircle2 = findViewById(R.id.password_circle_2);
        setupEditText(passwordCircle2);

        passwordCircle3 = findViewById(R.id.password_circle_3);
        setupEditText(passwordCircle3);

        passwordCircle4 = findViewById(R.id.password_circle_4);
        setupEditText(passwordCircle4);

        findViewById(R.id.button_0).setOnClickListener(btnListener);
        findViewById(R.id.button_1).setOnClickListener(btnListener);
        findViewById(R.id.button_2).setOnClickListener(btnListener);
        findViewById(R.id.button_3).setOnClickListener(btnListener);
        findViewById(R.id.button_4).setOnClickListener(btnListener);
        findViewById(R.id.button_5).setOnClickListener(btnListener);
        findViewById(R.id.button_6).setOnClickListener(btnListener);
        findViewById(R.id.button_7).setOnClickListener(btnListener);
        findViewById(R.id.button_8).setOnClickListener(btnListener);
        findViewById(R.id.button_9).setOnClickListener(btnListener);

        findViewById(R.id.button_clear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields();
            }
        });

        findViewById(R.id.button_erase).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteKey();
            }
        });

        if (type == AppLock.UNLOCK_PASSWORD || type == AppLock.DISABLE_PASSWORD) {
            passwordMessage.setText(R.string.enter_password);
        } else if (type == AppLock.ENABLE_PASSWORD) {
            passwordMessage.setText(R.string.enter_new_password);
        }

    }

    public void updateTheme() {
        if (Utility.getTheme(getApplication()) <= Utility.THEME_BLACK_WHITE) {
            setTheme(R.style.AppTheme_BlackWhite);
        } else if (Utility.getTheme(getApplication()) == Utility.THEME_WHITE_BLACK) {
            setTheme(R.style.AppTheme_WhiteBlack);
        }
    }

    public int getType() {
        return type;
    }

    protected void onPasswordEntered() {

        String passLock = passwordCircle1.getText().toString()
                + passwordCircle2.getText().toString()
                + passwordCircle3.getText().toString() + passwordCircle4.getText();

        switch (type) {

            case AppLock.DISABLE_PASSWORD:

                if (LockManager.getInstance().getAppLock().checkPassword(passLock)) {
                    setResult(RESULT_OK);
                    LockManager.getInstance().getAppLock().setPassword(null);
                    finish();

                    Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toastText.setText(R.string.password_disabled);
                    toastText.setTextColor(ColorPalette.GREEN.getColor());
                    toast.setView(toastLayout);
                    toast.show();

                } else {
                    onPasswordError();
                }
                break;

            case AppLock.ENABLE_PASSWORD:

                clearFields();
                if (oldPassword == null) {
                    passwordMessage.setText(R.string.reenter_new_password);
                    oldPassword = passLock;
                } else {
                    if (passLock.equals(oldPassword)) {
                        setResult(RESULT_OK);
                        LockManager.getInstance().getAppLock()
                                .setPassword(passLock);
                        finish();
                    } else {
                        oldPassword = null;
                        passwordMessage.setText(R.string.enter_new_password);
                        onPasswordError();
                    }
                }
                break;

            case AppLock.CHANGE_PASSWORD:
                clearFields();
                if (LockManager.getInstance().getAppLock().checkPassword(passLock)) {
                    passwordMessage.setText(R.string.enter_new_password);
                    type = AppLock.ENABLE_PASSWORD;
                } else {
                    onPasswordError();
                }
                break;

            case AppLock.UNLOCK_PASSWORD:
                if (LockManager.getInstance().getAppLock().checkPassword(passLock)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    onPasswordError();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (type == AppLock.UNLOCK_PASSWORD) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    protected void setupEditText(EditText editText) {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFilters(filters);
        editText.setOnTouchListener(touchListener);
        editText.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            onDeleteKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onDeleteKey() {

        if (passwordCircle2.isFocused()) {
            passwordCircle1.requestFocus();
            passwordCircle1.setText("");
            passwordCircle1.setBackgroundResource(R.drawable.password_circle_background);
        } else if (passwordCircle3.isFocused()) {
            passwordCircle2.requestFocus();
            passwordCircle2.setText("");
            passwordCircle2.setBackgroundResource(R.drawable.password_circle_background);
        } else if (passwordCircle4.isFocused()) {
            passwordCircle3.requestFocus();
            passwordCircle3.setText("");
            passwordCircle3.setBackgroundResource(R.drawable.password_circle_background);
        }

    }

    private OnClickListener btnListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            int currentValue = -1;

            switch (view.getId()) {
                case R.id.button_0:
                    currentValue = 0;
                    break;
                case R.id.button_1:
                    currentValue = 1;
                    break;
                case R.id.button_2:
                    currentValue = 2;
                    break;
                case R.id.button_3:
                    currentValue = 3;
                    break;
                case R.id.button_4:
                    currentValue = 4;
                    break;
                case R.id.button_5:
                    currentValue = 5;
                    break;
                case R.id.button_6:
                    currentValue = 6;
                    break;
                case R.id.button_7:
                    currentValue = 7;
                    break;
                case R.id.button_8:
                    currentValue = 8;
                    break;
                case R.id.button_9:
                    currentValue = 9;
                    break;
            }

            String currentValueString = String.valueOf(currentValue);

            if (passwordCircle1.isFocused()) {
                passwordCircle1.setText(currentValueString);
                passwordCircle1.setBackgroundResource(R.drawable.password_circle_background_entered);
                passwordCircle2.requestFocus();
                passwordCircle2.setText("");

            } else if (passwordCircle2.isFocused()) {

                passwordCircle2.setText(currentValueString);
                passwordCircle2.setBackgroundResource(R.drawable.password_circle_background_entered);
                passwordCircle3.requestFocus();
                passwordCircle3.setText("");

            } else if (passwordCircle3.isFocused()) {

                passwordCircle3.setText(currentValueString);
                passwordCircle3.setBackgroundResource(R.drawable.password_circle_background_entered);
                passwordCircle4.requestFocus();
                passwordCircle4.setText("");

            } else if (passwordCircle4.isFocused()) {

                passwordCircle4.setText(currentValueString);
                passwordCircle4.setBackgroundResource(R.drawable.password_circle_background_entered);

            }

            if (passwordCircle4.getText().toString().length() > 0
                    && passwordCircle3.getText().toString().length() > 0
                    && passwordCircle2.getText().toString().length() > 0
                    && passwordCircle1.getText().toString().length() > 0) {
                onPasswordEntered();
            }
        }
    };

    protected void onPasswordError() {

        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL, 0, 30);
        toastText.setText(getString(R.string.password_wrong));
        toastText.setTextColor(ColorPalette.RED.getColor());
        toast.setView(toastLayout);
        toast.show();

        clearFields();
    }

    private InputFilter numberFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            if (source.length() > 1) {
                return "";
            }

            if (source.length() == 0) {
                return null;
            }

            try {
                int number = Integer.parseInt(source.toString());
                if ((number >= 0) && (number <= 9))
                    return String.valueOf(number);
                else
                    return "";
            } catch (NumberFormatException e) {
                return "";
            }
        }
    };

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (passwordCircle1.isFocused()) {
                passwordCircle1.requestFocus();
            } else if (passwordCircle2.isFocused()) {
                passwordCircle2.requestFocus();
            } else if (passwordCircle3.isFocused()) {
                passwordCircle3.requestFocus();
            } else if (passwordCircle4.isFocused()) {
                passwordCircle4.requestFocus();
            }

            return true;
        }
    };

    private void clearFields() {

        passwordCircle1.requestFocus();
        passwordCircle1.setText("");
        passwordCircle1.setBackgroundResource(R.drawable.password_circle_background);
        passwordCircle2.setText("");
        passwordCircle2.setBackgroundResource(R.drawable.password_circle_background);
        passwordCircle3.setText("");
        passwordCircle3.setBackgroundResource(R.drawable.password_circle_background);
        passwordCircle4.setText("");
        passwordCircle4.setBackgroundResource(R.drawable.password_circle_background);

    }
}