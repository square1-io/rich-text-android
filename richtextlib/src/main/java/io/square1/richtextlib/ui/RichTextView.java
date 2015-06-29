package io.square1.richtextlib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import io.square1.richtextlib.style.ClickableSpan;
import io.square1.richtextlib.style.P2ParcelableSpan;

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

    }

}
