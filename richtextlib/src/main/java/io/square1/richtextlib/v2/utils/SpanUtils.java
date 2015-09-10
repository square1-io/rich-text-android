package io.square1.richtextlib.v2.utils;

import android.view.View;

import io.square1.richtextlib.style.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichTextView;

/**
 * Created by roberto on 08/09/15.
 */
public class SpanUtils {

    public static UrlBitmapDownloader getDownloader(View view){

        if(view == null) return null;

        if(view instanceof RichTextView){
            return ((RichTextView)view).getDownloader();
        }

        return null;
    }

}
