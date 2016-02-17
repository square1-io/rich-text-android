/*
 * Copyright (c) 2015. Roberto  Prato <https://github.com/robertoprato>
 *
 *  *
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package io.square1.richtextlib.ui;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.res.TypedArray;

import android.graphics.Movie;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.widget.TextView;

<<<<<<< HEAD

import io.square1.richtextlib.style.ClickableSpan;
import io.square1.richtextlib.style.P2ParcelableSpan;
import io.square1.richtextlib.style.Style;
import io.square1.richtextlib.style.URLSpan;
import io.square1.richtextlib.style.UnsupportedContentSpan;
import io.square1.richtextlib.style.UrlBitmapDownloader;
import io.square1.richtextlib.style.YouTubeSpan;
=======
>>>>>>> ab90cbb48ff8f0c9b26f4ca97aa2e17ed5baf703
import io.square1.richtextlib.R;


/**
 * Created by roberto on 24/06/15.
 */
@Deprecated
public class RichTextView extends TextView  {


<<<<<<< HEAD
    private Style mStyle;

    public void setStyle(Style style) {
        mStyle = style;
    }

    public Style getStyle(){
        return mStyle;
    }
    
    public interface OnSpanClickedObserver {
         boolean onSpanClicked(ClickableSpan span);
    }

    public interface RichTextContentChanged {
        void onContentChanged(RichTextView view);
    }

    private P2ParcelableSpan[] mSpans;
    private Spannable mSpannable;
    private RichTextContentChanged mRichTextContentChanged;
    private OnSpanClickedObserver mOnSpanClickedObserver;
    private Handler mHandler;
    private boolean mAttachedToWindow;
    private UrlBitmapDownloader mDownloader;
=======
>>>>>>> ab90cbb48ff8f0c9b26f4ca97aa2e17ed5baf703

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseCustomAttributes(context, attrs);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseCustomAttributes(context, attrs);
    }


    @TargetApi(21)
    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseCustomAttributes(context, attrs);
    }


    public RichTextView(Context context) {
        super(context);
<<<<<<< HEAD
        init();
    }


    private void init(){
        mAttachedToWindow = false;
        setMovementMethod( new RichTextLinkMovementMethod(this));
        mSpans = getAllSpans();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                invalidate();

            }
        };

    }

    public UrlBitmapDownloader getDownloader(){
        return mDownloader;
    }

    public void setUrlBitmapDownloader(UrlBitmapDownloader downloader){
        mDownloader = downloader;
    }

   @Override
    public void onAttachedToWindow(){
       mAttachedToWindow = true;
       super.onAttachedToWindow();

       for(P2ParcelableSpan span : mSpans){
           span.onAttachedToView(this);
       }
   }

    @Override
    public void onDetachedFromWindow(){
        mAttachedToWindow = false;
        super.onDetachedFromWindow();

        for(P2ParcelableSpan span : mSpans){
            span.onDetachedFromView(this);
        }
    }

    public Spannable getSpannable(){
        return mSpannable;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text,BufferType.SPANNABLE);

        mSpans = getAllSpans();

        if(text != null && text instanceof Spannable){
            getEditableText();
            mSpannable = (Spannable)text;
            for(P2ParcelableSpan span : mSpans){
                span.onSpannedSetToView(this);
            }
        }
    }

    public void setRichTextContentChanged(RichTextContentChanged listener){
        mRichTextContentChanged = listener;
    }

    public void setOnSpanClickedObserver(OnSpanClickedObserver listener){
        mOnSpanClickedObserver = listener;
    }

    public void notifyContentChanged(){

        mHandler.sendEmptyMessage(0);

        if(mRichTextContentChanged != null){
            mRichTextContentChanged.onContentChanged(this);
        }

    }

    public P2ParcelableSpan[] getSpans(){
        if(mSpans == null){
            mSpans = getAllSpans();
        }
        return mSpans;
    }

    private P2ParcelableSpan[] getAllSpans(){

        final CharSequence sequence = getText();
        if(sequence != null && sequence instanceof Spanned){
           return  ((Spanned) sequence).getSpans(0,sequence.length(),P2ParcelableSpan.class);
        }

        return new P2ParcelableSpan[0];
    }

    @Override
    public void onSpansClicked(ClickableSpan[] spans) {

        if(spans == null) return;

        for(ClickableSpan span : spans){

            //if handled externally lets just continue
            if(mOnSpanClickedObserver != null &&
                    mOnSpanClickedObserver.onSpanClicked(span) == true)
                continue;

            if(span instanceof YouTubeSpan){

                String id = ((YouTubeSpan)span).getYoutubeId();

                this.getContext().
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + id)));
            }
            else if(span instanceof URLSpan){

                try {
                    String url = ((URLSpan) span).getURL();
                    this.getContext().
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }catch(ActivityNotFoundException e) {

                }

            }else if (span instanceof UnsupportedContentSpan){
                String url = ((UnsupportedContentSpan)span).getURL();
                WebDialog dialog = new WebDialog(getContext(),url);
                dialog.setCancelable(true);
                dialog.show();

            }
        }

    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        /// this really needs to be checked for the impact it could have on performances
        invalidate();
    }

    public final boolean isAttachedToWindow(){
        return mAttachedToWindow;
=======
>>>>>>> ab90cbb48ff8f0c9b26f4ca97aa2e17ed5baf703
    }


    private void parseCustomAttributes(Context ctx, AttributeSet attrs) {
       // TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.io_square1_richtextlib_ui_RichTextView);
       // String customFont = a.getString(R.styleable.io_square1_richtextlib_ui_RichTextView_fontName);
       // setCustomFont(ctx, customFont);
       // a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {

        if(TextUtils.isEmpty(asset)){
            return false;
        }

        try {

            Typeface tf  = Typeface.createFromAsset(ctx.getAssets(), asset);
            setTypeface(tf);

        } catch (Exception e) {

            return false;

        }

        return true;
    }

}