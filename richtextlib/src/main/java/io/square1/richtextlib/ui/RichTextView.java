package io.square1.richtextlib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;

import io.square1.richtextlib.style.P2ParcelableSpan;

/**
 * Created by roberto on 24/06/15.
 */
public class RichTextView extends TextView {

    public interface RichTextContentChanged {
        void onContentChanged(RichTextView view);
    }

    private P2ParcelableSpan[] mSpans;
    private Spannable mSpannable;
    private RichTextContentChanged mRichTextContentChanged;

    public RichTextView(Context context) {
        super(context);
        mSpans = getAllSpans();
        setWillNotCacheDrawing(true);
        //setWillNotDraw(false);
    }

   @Override
    public void onAttachedToWindow(){
       super.onAttachedToWindow();

       for(P2ParcelableSpan span : mSpans){
           span.onAttachedToView(this);
       }
   }

    @Override
    public void onDetachedFromWindow(){
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
        super.setText(text,BufferType.NORMAL);

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
