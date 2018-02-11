/*
 * Copyright (c) 2016. Roberto  Prato <https://github.com/robertoprato>
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

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.View;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.Appearance;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

public class HeaderSpan extends MetricAffectingSpan implements RichTextSpan {

    public static final Creator<HeaderSpan> CREATOR  = DynamicParcelableCreator.getInstance(HeaderSpan.class);

    public static final int TYPE = UniqueId.getType();

    @Override
    public int getType() {
        return TYPE;
    }

    private int  mHeaderIndex;
    private TextPaint mTextPaint;

    public HeaderSpan(){

    }

    public HeaderSpan(int index) {
        mHeaderIndex = index;
    }

    public HeaderSpan(Parcel src) {
      readFromParcel(src);
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeInt(mHeaderIndex);
    }


    @Override
    public void updateDrawState(TextPaint ds) {

       if(mTextPaint != null){
           ds.set(mTextPaint);
       }
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        if(mTextPaint != null){
            ds.set(mTextPaint);
        }
    }


    @Override
    public void readFromParcel(Parcel src) {
        mHeaderIndex = src.readInt();
    }

    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {

    }

    @Override
    public void onDetachedFromWindow(RichContentViewDisplay view) {

    }

    @Override
    public void onSpannedSetToView(RichContentView view){
        final Appearance appearance = view.getStyle();
        mTextPaint = appearance.textPaintForHeader(mHeaderIndex);
    }


    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {

    }
}