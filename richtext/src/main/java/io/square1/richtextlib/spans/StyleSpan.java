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

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.View;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

/**
 *
 * Describes a style in a span.
 * Note that styles are cumulative -- if both bold and italic are set in
 * separate spans, or if the base style is bold and a span calls for italic,
 * you getInstance bold italic.  You can't turn off a style from the base style.
 *
 */
public class StyleSpan extends MetricAffectingSpan implements RichTextSpan {

    public static final Creator<StyleSpan> CREATOR  = DynamicParcelableCreator.getInstance(StyleSpan.class);

    public static final int TYPE = UniqueId.getType();

    public StyleSpan(){}

    @Override
    public int getType() {
        return TYPE;
    }

    private  int mStyle;


    /**
     *
     * @param style An integer constant describing the style for this span. Examples
     * include bold, italic, and normal. Values are constants defined
     * in {@link Typeface}.
     */
    public StyleSpan(int style) {
        mStyle = style;
    }

    public StyleSpan(Parcel src) {
       readFromParcel(src);
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeInt(mStyle + 1);
    }

    /**
     * Returns the style constant defined in {@link Typeface}.
     */
    public int getStyle() {
        return mStyle;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        apply(ds, mStyle);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        apply(paint, mStyle);
    }

    private static void apply(Paint paint, int style) {
        int oldStyle;

        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int want = oldStyle | style;

        Typeface tf;
        if (old == null) {
            tf = Typeface.defaultFromStyle(want);
        } else {
            tf = Typeface.create(old, want);
        }

        int fake = want & ~tf.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }

    @Override
    public void readFromParcel(Parcel src) {
        mStyle = src.readInt();
        mStyle = mStyle - 1;
    }

    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {

    }

    @Override
    public void onDetachedFromWindow(RichContentViewDisplay view) {

    }

    @Override
    public void onSpannedSetToView(RichContentView view){

    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {

    }
}
