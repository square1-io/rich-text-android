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


    /**
     * Bitmap image used when showing a quote
     * @return
     */
     Bitmap quoteBitmap();
     int headerColor();


}
