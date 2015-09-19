package io.square1.richtextlib.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;

import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 19/09/15.
 */
public class LeadingMarginSpan implements P2ParcelableSpan, android.text.style.LeadingMarginSpan {

    public static final Parcelable.Creator<LeadingMarginSpan> CREATOR  = P2ParcelableCreator.get(LeadingMarginSpan.class);
    public static final int TYPE = UniqueId.getType();

    private int mFirstLine;
    private int mRest;

    public LeadingMarginSpan(int first, int rest){
        mFirstLine = first;
        mRest = rest;
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
    public void onSpannedSetToView(RichTextView view) {

    }

    @Override
    public void onAttachedToView(RichTextView view) {

    }

    @Override
    public void onDetachedFromView(RichTextView view) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mFirstLine);
        dest.writeInt(mRest);
    }
}
