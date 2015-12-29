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

package io.square1.richtextlib.spans;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;


public class QuoteSpan extends MetricAffectingSpan implements /*LineHeightSpan,*/ LineBackgroundSpan,RichTextSpan,LeadingMarginSpan {

    public static final Parcelable.Creator<QuoteSpan> CREATOR  = DynamicParcelableCreator.getInstance(QuoteSpan.class);
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
        DynamicParcelableCreator.writeType(dest, this);
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
        p.setColor(mColor);
        p.setStyle(Paint.Style.FILL);
        Rect rect = new Rect(left, top, right, bottom);
        c.drawRect(rect, p);


        p.setColor(paintColor);
    }

    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {

    }

    @Override
    public void onDetachedFromWindow(RichContentViewDisplay view) {

    }

    @Override
    public void onSpannedSetToView(RichContentViewDisplay view){

    }
}