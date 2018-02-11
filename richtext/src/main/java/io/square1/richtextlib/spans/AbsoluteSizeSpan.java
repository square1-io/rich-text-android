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

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.View;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

public class AbsoluteSizeSpan extends MetricAffectingSpan implements RichTextSpan {

    public static final  int TYPE = UniqueId.getType();

    @Override
    public int getType() {
        return TYPE;
    }

    public static final Creator<AbsoluteSizeSpan> CREATOR  = DynamicParcelableCreator.getInstance(AbsoluteSizeSpan.class);

    private  int mSize;
    private boolean mDip;

    public AbsoluteSizeSpan(int size) {
        mSize = size;
    }


    public AbsoluteSizeSpan(int size, boolean dip) {
        mSize = size;
        mDip = dip;
    }

    public AbsoluteSizeSpan(Parcel src) {
      readFromParcel(src);
    }
    

    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeInt(mSize);
        dest.writeInt(mDip ? 1 : 0);
    }

    public int getSize() {
        return mSize;
    }

    public boolean getDip() {
        return mDip;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (mDip) {
            ds.setTextSize(mSize * ds.density);
        } else {
            ds.setTextSize(mSize);
        }
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        if (mDip) {
            ds.setTextSize(mSize * ds.density);
        } else {
            ds.setTextSize(mSize);
        }
    }

    @Override
    public void readFromParcel(Parcel src) {
        mSize = src.readInt();
        mDip = src.readInt() != 0;
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