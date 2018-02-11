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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.view.View;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 19/09/15.
 */
public class LeadingMarginSpan implements RichTextSpan, android.text.style.LeadingMarginSpan {

    public static final Parcelable.Creator<LeadingMarginSpan> CREATOR  = DynamicParcelableCreator.getInstance(LeadingMarginSpan.class);
    public static final int TYPE = UniqueId.getType();

    private int mFirstLine;
    private int mRest;

    public LeadingMarginSpan(int first, int rest){
        mFirstLine = first;
        mRest = rest;
    }


    public LeadingMarginSpan(){
        super();
        mFirstLine = mRest = 0;
    }


    @Override
    public int getLeadingMargin(boolean first) {
        return first ? mFirstLine : mRest;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {

    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mFirstLine = src.readInt();
        mRest = src.readInt();
    }


    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {

    }

    @Override
    public void onDetachedFromWindow(RichContentViewDisplay view) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeInt(mFirstLine);
        dest.writeInt(mRest);
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
