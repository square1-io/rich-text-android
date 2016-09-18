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
import io.square1.richtextlib.ui.Appearance;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;


public class QuoteSpan extends MetricAffectingSpan implements /*LineHeightSpan,*/ LineBackgroundSpan,RichTextSpan,LeadingMarginSpan {

    public static final Parcelable.Creator<QuoteSpan> CREATOR  = DynamicParcelableCreator.getInstance(QuoteSpan.class);
    public static final int TYPE = UniqueId.getType();



    @Override
    public int getType() {
        return TYPE;
    }


    private Bitmap mQuoteSign;
    private int mColor;
    private int mLinesCount;

    private  float mSignLeftPadding = 0;
    private  float mSignRightPadding = 0;
    private  float mSignTopPadding = 0;


    public QuoteSpan() {
       this(0xff0000ff,null);
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



    @Override
    public void updateDrawState(TextPaint p) {

    }

    @Override
    public void updateMeasureState(TextPaint p) {

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
    }

    public void readFromParcel(Parcel src){
        mColor = src.readInt();

    }


    public int getLeadingMargin(boolean first) {

        if(mQuoteSign != null) {

            return mQuoteSign.getWidth() +
                    (int)mSignLeftPadding +
                    (int)mSignRightPadding;

        }
        return  (int)mSignLeftPadding +
                (int)mSignRightPadding;
    }

    public float getBitmapH(Bitmap bmp){
        if(mQuoteSign != null) {
            return mQuoteSign.getHeight();
        }
        return 0;
    }

    public float getBitmapW(Bitmap bmp){

        if(mQuoteSign != null) {
            return mQuoteSign.getWidth();
        }
        return 0;
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

        if (dir < 0) {
            x -= getBitmapW(mQuoteSign);
        }

        if(mQuoteSign != null) {
            c.drawBitmap(mQuoteSign, x + mSignLeftPadding, itop + mSignTopPadding, p);
        }

    }

    @Override
    public void drawBackground(Canvas c,
                               Paint p,
                               int left,
                               int right,
                               int top,
                               int baseline,
                               int bottom,
                               CharSequence text,
                               int start,
                               int end,
                               int lnum) {

        if(mLinesCount < 0){
            mLinesCount = lnum;
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

        final Appearance appearance = view.getStyle();
        mColor = appearance.getQuoteBackgroundColor();
        mQuoteSign = appearance.getQuoteSign();
        mSignLeftPadding = appearance.getQuoteSignLeftPadding();
        mSignRightPadding = appearance.getQuoteSignRightPadding();
        mSignTopPadding = appearance.getQuoteSignTopPadding();
    }
}