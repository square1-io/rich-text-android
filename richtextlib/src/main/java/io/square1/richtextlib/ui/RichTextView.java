package io.square1.richtextlib.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import io.square1.richtextlib.style.ClickableSpan;
import io.square1.richtextlib.style.P2ParcelableSpan;
import io.square1.richtextlib.style.YouTubeSpan;

/**
 * Created by roberto on 24/06/15.
 */
public class RichTextView extends TextView implements RichTextLinkMovementMethod.Observer {


    public interface RichTextContentChanged {
        void onContentChanged(RichTextView view);
    }

    private P2ParcelableSpan[] mSpans;
    private Spannable mSpannable;
    private RichTextContentChanged mRichTextContentChanged;
    private Handler mHandler;
    private boolean mAttachedToWindow;

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    public RichTextView(Context context) {
        super(context);
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

            if(span instanceof YouTubeSpan){
                String id = ((YouTubeSpan)span).getYoutubeId();
                this.getContext().
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id)));
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
    }

}
