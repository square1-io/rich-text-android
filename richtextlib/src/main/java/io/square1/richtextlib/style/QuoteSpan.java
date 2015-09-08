package io.square1.richtextlib.style;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.MetricAffectingSpan;

import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.UniqueId;


public class QuoteSpan extends MetricAffectingSpan implements /*LineHeightSpan,*/ LineBackgroundSpan,P2ParcelableSpan,LeadingMarginSpan {

    public static final Parcelable.Creator<QuoteSpan> CREATOR  = P2ParcelableCreator.get(QuoteSpan.class);
    public static final int TYPE = UniqueId.getType();



    @Override
    public int getType() {
        return TYPE;
    }

    private static final int STRIPE_WIDTH = 2;
    private static final int STRIPE_PADDING = 4;
    private static final int GAP_WIDTH = 10;

    private Bitmap mQuoteSign;
    private int mColor;
    private int mLinesCount;
    private int mCurrentLine;

    public QuoteSpan() {

       this(0xff0000ff,
               Bitmap.createBitmap(10,10, Bitmap.Config.RGB_565));
    }

    public QuoteSpan(Bitmap quoteSign) {
        this(0xff0000ff, quoteSign);
    }

    public QuoteSpan(int color , Bitmap quoteSign) {
        super();
        mQuoteSign = quoteSign;
        mColor = color;
        mLinesCount = -1;
    }

    private float mInitialTextSize = -1;

    @Override
    public void updateDrawState(TextPaint p) {

        if(mInitialTextSize < 0){
            mInitialTextSize = p.getTextSize();
        }

        p.setTextSize(mInitialTextSize);

    }

    @Override
    public void updateMeasureState(TextPaint p) {
        if(mInitialTextSize < 0){
            mInitialTextSize = p.getTextSize();
        }
        p.setTextSize(mInitialTextSize * 1.1f);
    }


    public QuoteSpan(int color) {
        super();
        mColor = color;

    }

    public QuoteSpan(Parcel src) {
       readFromParcel(src);
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        P2ParcelUtils.writeType(dest,this);
        dest.writeInt(mColor);
        dest.writeParcelable(mQuoteSign, 0);
    }

    public void readFromParcel(Parcel src){
        mColor = src.readInt();
        mQuoteSign = src.readParcelable(Bitmap.class.getClassLoader());
    }


    public int getLeadingMargin(boolean first) {
        return  mQuoteSign.getWidth() + GAP_WIDTH;
    }

    public int getBitmapH(Bitmap bmp){
        return mQuoteSign.getHeight() + STRIPE_PADDING;
    }

    public int getBitmapW(Bitmap bmp){
        return mQuoteSign.getWidth() + STRIPE_PADDING;
    }

    public void drawLeadingMargin(Canvas c,
                                  Paint p,
                                  int x,
                                  int dir,
                                  int top,
                                  int baseline,
                                  int bottom,
                                  CharSequence text,
                                  int start,
                                  int end,
                                  boolean first,
                                  Layout layout) {

        int st = ((Spanned) text).getSpanStart(this);

        int itop = layout.getLineTop(layout.getLineForOffset(st));
        if (dir < 0)
            x -= getBitmapW(mQuoteSign);
        c.drawBitmap(mQuoteSign, x + STRIPE_PADDING, itop + STRIPE_PADDING, p);

    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {

        if(mLinesCount < 0){
            mLinesCount = lnum;
            mCurrentLine = 1;
        }else {
            mCurrentLine ++;
        }

        final int paintColor = p.getColor();
        p.setColor(mColor);//Color.LTGRAY);
        p.setStyle(Paint.Style.FILL);
        Rect rect = new Rect(left, top, right, bottom);
        c.drawRect(rect, p);
//
//        p.setStyle(Paint.Style.STROKE);
//        p.setColor(Color.BLACK);
//
//        int borderSectionsCount = 0;
//        float[] borderData = new float[ 3 * 4 ];// 4 values per line is the max we are going to have
//        //top border for first line
//        if(mCurrentLine == 1){
//            borderData[borderSectionsCount] = left;
//            borderData[borderSectionsCount + 1] = top;
//            borderData[borderSectionsCount + 2] = right;
//            borderData[borderSectionsCount + 3] = top;
//            borderSectionsCount ++;
//        }
//
//        if(mCurrentLine == mLinesCount){
//            borderData[borderSectionsCount] = left;
//            borderData[borderSectionsCount + 1] = bottom;
//            borderData[borderSectionsCount + 2] = right;
//            borderData[borderSectionsCount + 3] = bottom;
//            borderSectionsCount ++;
//        }
//
//        c.drawLines(borderData, 0,borderSectionsCount, p);

        p.setColor(paintColor);
    }

    public void chooseHeight(CharSequence text, int start, int end,
                             int istartv, int v,
                             Paint.FontMetricsInt fm) {
        if (end == ((Spanned) text).getSpanEnd(this)) {
            int ht = getBitmapH(mQuoteSign);
            int need = ht - (v + fm.descent - fm.ascent - istartv);
            if (need > 0)
                fm.descent += need;
            need = ht - (v + fm.bottom - fm.top - istartv);
            if (need > 0)
                fm.bottom += need;
        }
    }

    @Override
    public void onAttachedToView(RichTextView view) {

    }

    @Override
    public void onDetachedFromView(RichTextView view) {

    }

    @Override
    public void onSpannedSetToView(RichTextView view){

    }
}