package com.vb.tracker.free.colorpicker;

public enum ColorPalette {

    AMBER(0xffFFC107),
    ORANGE(0xffFF9800),
    DEEP_ORANGE(0xffFF5722),
    RED(0xffF00001),
    PINK(0xffE91E63),
    PURPLE(0xff9C27B0),
    BLUE(0xff2196F3),
    LIGHT_BLUE(0xff03A9F4),
    CYAN(0xff00BCD4),
    TEAL(0xff009688),
    GREEN(0xff4CAF50),
    LIGHT_GREEN(0xff8BC34A),
    LIME(0xffCDDC39);

    private int color;

    ColorPalette(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
