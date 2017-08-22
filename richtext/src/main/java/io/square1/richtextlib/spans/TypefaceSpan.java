
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
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.View;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

/**
 * Changes the typeface family of the text to which the span is attached.
 */
public class TypefaceSpan extends MetricAffectingSpan implements RichTextSpan {

    public static final Parcelable.Creator<TypefaceSpan> CREATOR  = DynamicParcelableCreator.getInstance(TypefaceSpan.class);

    public static final int TYPE = UniqueId.getType();

    @Override
    public int getType() {
        return TYPE;
    }

    private  String mFamily;
    private  Typeface mTypeFace;

    /**
     * @param family The font family for this typeface.  Examples include
     * "monospace", "serif", and "sans-serif".
     */
    public TypefaceSpan(String family) {
        mFamily = family;
    }

    public TypefaceSpan(Parcel src) {
        readFromParcel(src);
    }

    public TypefaceSpan(){}

    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeString(mFamily);
    }

    /**
     * Returns the font family name.
     */
    public String getFamily() {
        return mFamily;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        apply(ds,mTypeFace, mFamily);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        apply(paint,mTypeFace, mFamily);
    }

    private static void apply(Paint paint, Typeface typeFace,  String family) {
        int oldStyle;

        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        if(typeFace == null) {
            typeFace = Typeface.create(family, oldStyle);
        }
        int fake = oldStyle & ~typeFace.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(typeFace);
    }

    @Override
    public void readFromParcel(Parcel src) {
        mFamily = src.readString();
    }

    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {

    }

    @Override
    public void onDetachedFromWindow(RichContentViewDisplay view) {

    }

    @Override
    public void onSpannedSetToView(RichContentView view){
        mTypeFace = view.getTypeFace(mFamily);
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {

    }
}
