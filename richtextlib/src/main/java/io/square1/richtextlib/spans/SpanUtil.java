package io.square1.richtextlib.spans;

import java.lang.ref.WeakReference;

import io.square1.richtextlib.ui.RichContentViewDisplay;

/**
 * Created by roberto on 30/09/15.
 */
public class SpanUtil {

    public static UrlBitmapDownloader get(WeakReference<RichContentViewDisplay> viewWeakReference) {

        if(viewWeakReference == null) return null;

        RichContentViewDisplay view = viewWeakReference.get();

        if(view != null) return view.getDownloader();

        return null;

    }

}
