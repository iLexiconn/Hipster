package net.ilexiconn.hipster.util;

import android.graphics.Color;

public class ColorUtil {
    public static int darker(int color, float factor) {
        return Color.argb(Color.alpha(color), Math.max((int) (Color.red(color) * factor), 0), Math.max((int) (Color.green(color) * factor), 0), Math.max((int) (Color.blue(color) * factor), 0));
    }
}
