package io.square1.richtextlib.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;


import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.ClickableSpan;
import io.square1.richtextlib.style.P2ParcelableSpan;
import io.square1.richtextlib.style.URLSpan;
import io.square1.richtextlib.style.UnsupportedContentSpan;
import io.square1.richtextlib.style.UrlBitmapDownloader;
import io.square1.richtextlib.style.YouTubeSpan;
import io.square1.richtextlib.R;
/**
 * Created by roberto on 24/06/15.
 */
public interface  RichTextView extends Drawable.Callback, RichTextLinkMovementMethod.Observer {


    void setText(ParcelableSpannedBuilder content);
    void setUrlBitmapDownloader(UrlBitmapDownloader downloader);
    void setRichTextContentChanged(RichTextContentChanged richTextContentChanged);

    public interface OnSpanClickedObserver {
         boolean onSpanClicked(ClickableSpan span);
    }

    public interface RichTextContentChanged {
        void onContentChanged(RichTextView view);
    }


     UrlBitmapDownloader getDownloader();

    void invalidate();
    void requestLayout();
    boolean viewAttachedToWindow();
    int getMeasuredWidth();
     void performLayout();
    Context getContext();


}
