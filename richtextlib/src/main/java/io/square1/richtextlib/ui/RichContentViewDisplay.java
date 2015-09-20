package io.square1.richtextlib.ui;

import android.content.Context;

import android.graphics.drawable.Drawable;


import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.ClickableSpan;
import io.square1.richtextlib.style.UrlBitmapDownloader;

/**
 * Created by roberto on 24/06/15.
 */
public interface RichContentViewDisplay extends Drawable.Callback {


    void setText(ParcelableSpannedBuilder content);
    void setUrlBitmapDownloader(UrlBitmapDownloader downloader);
    void setRichTextContentChanged(RichTextContentChanged richTextContentChanged);

    public interface OnSpanClickedObserver {
         boolean onSpanClicked(ClickableSpan span);
    }

    public interface RichTextContentChanged {
        void onContentChanged(RichContentViewDisplay view);
    }


     UrlBitmapDownloader getDownloader();

    void invalidate();
    void requestLayout();
    boolean viewAttachedToWindow();
    int getMeasuredWidth();
     void performLayout();
    Context getContext();


}
