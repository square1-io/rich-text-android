package io.square1.richtextlib.style;

import android.content.Context;
import android.graphics.Bitmap;




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
     * get the background color for a quote tag
     * @return
     */
    int getQuoteBackgroundColor();

    /**
     * text color to be used for H1... headers
     */
     int headerColor();

    /**
     * The color to be used as the main background
     * @return
     */
    int backgroundColor();
    int maxImageWidth();
    int maxImageHeight();
}
