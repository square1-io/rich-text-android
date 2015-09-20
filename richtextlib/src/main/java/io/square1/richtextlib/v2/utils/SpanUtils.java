package io.square1.richtextlib.v2.utils;

import io.square1.richtextlib.style.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentViewDisplay;

/**
 * Created by roberto on 08/09/15.
 */
public class SpanUtils {

    public static UrlBitmapDownloader getDownloader(RichContentViewDisplay view){

        if(view == null) return null;

        if(view instanceof RichContentViewDisplay){
            return ((RichContentViewDisplay)view).getDownloader();
        }

        return null;
    }

}
