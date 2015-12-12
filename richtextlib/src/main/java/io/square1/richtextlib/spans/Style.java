package io.square1.richtextlib.spans;

import android.content.Context;
import android.graphics.Bitmap;




/**
 * Created by roberto on 17/06/15.
 */
public interface Style {


    static final int NOT_SET = Integer.MIN_VALUE;
    static final int USE_DEFAULT = Integer.MIN_VALUE;

    Context getApplicationContext();

    /**
     * Bitmap image used when showing a quote
     * @return
     */
     Bitmap quoteBitmap();

    /**
     * getInstance the background color for a quote tag
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

    /**
     * the max width for images to be displayed
     * @return
     */
    int maxImageWidth();

    int maxImageHeight();

    /**
     * The multiplier for the text increase for Headers
     *
     * @param headerLevel from 1 to 6
     * @return 1.2 , 1.3 ecc ecc
     *
     */
    float headerIncrease(int headerLevel);

    /**
     * the text reduction to apply when the <SMALL></SMALL> tag is encountered.
     * @return < 1
     */
    float  smallTextReduce();

    /**
     * will make an attempt to resolve Wordpress tags such as [soundcloud /]
     */
    boolean parseWordPressTags();

    /**
     * when parsing pure HTML white spaces and new lines have to be considered differently
     * @return
     */
    boolean treatAsHtml();
}
