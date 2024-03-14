package com.main;
import android.content.Context;
import android.graphics.Typeface;

public class FontManager {

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fa_solid_900.ttf"; // Replace with the actual font file name

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}
