package com.vb.tracker.free.colorpicker;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vb.tracker.R;
import com.vb.tracker.free.Utility;

import java.util.ArrayList;
import java.util.List;

public class ColorPicker extends BottomSheetDialogFragment {

    View v;
    private List<Integer> colors = new ArrayList<>();
    private List<ImageView> buttons = new ArrayList<>();
    private ColorListener colorListener;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (colorListener != null) colorListener.OnColorClick(v, (int) v.getTag());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = inflater.inflate(R.layout.color_picker, container, false);
        Utility.updateTheme(v.getContext());
        for (ColorPalette color : ColorPalette.values()) {
            createColorCircle(color.getColor());
        }

        setListeners();

        return v;
    }

    private ImageView createCircle(int color) {

        ImageView colorCircle = new ImageView(v.getContext());

        final int scale = (int) getContext().getResources().getDisplayMetrics().density;

        LinearLayout layout = v.findViewById(R.id.color_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 7 * scale, 0);

        layout.addView(colorCircle, params);

        colorCircle.getLayoutParams().height = 70 * scale;
        colorCircle.getLayoutParams().width = 70 * scale;

        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(29 * scale, 29 * scale, 29 * scale, 29 * scale);

        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(color);

        colorCircle.setBackground(d);

        return colorCircle;
    }

    private void createColorCircle(int color) {
        colors.add(color);
        buttons.add(createCircle(color));
    }

    private void setListeners() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setTag(colors.get(i));
            buttons.get(i).setOnClickListener(listener);
        }
    }

    public void setColorListener(ColorListener listener) {
        this.colorListener = listener;
    }

}
