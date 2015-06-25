package io.square1.richtextlib.style;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import io.square1.richtextlib.R;

/**
 * Created by roberto on 17/06/15.
 */
public interface Style {

    int USE_DEFAULT = Integer.MIN_VALUE;

    Context getApplicationContext();

    /**
     * Bitmap image used when showing a quote
     * @return
     */
     Bitmap quoteBitmap();

    /**
     * text color to be used for H1... headers
     */
     int headerColor();
    int maxImageWidth();
    int maxImageHeight();
}
