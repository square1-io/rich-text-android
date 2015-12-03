package io.square1.richtextlib.spans;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import io.square1.parcelable.DynamicParcelableCreator;
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
    public void onAttachedToView(RichContentViewDisplay view) {

    }

    @Override
    public void onDetachedFromView(RichContentViewDisplay view) {

    }

    @Override
    public void onSpannedSetToView(RichContentViewDisplay view){

    }
}