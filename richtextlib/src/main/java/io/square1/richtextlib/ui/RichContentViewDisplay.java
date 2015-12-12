package io.square1.richtextlib.ui;

import android.content.Context;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;


import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.ClickableSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;

/**
 * Created by roberto on 24/06/15.
 */
public interface RichContentViewDisplay extends Drawable.Callback {


    Point getSpanOrigin(Object span);
    void addSubView(View view);
    void setText(RichTextDocumentElement content);
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

    public int getPaddingLeft();
    public int getPaddingRight();


}
