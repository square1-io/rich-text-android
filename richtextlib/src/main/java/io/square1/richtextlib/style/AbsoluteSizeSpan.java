package io.square1.richtextlib.style;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class AbsoluteSizeSpan extends MetricAffectingSpan implements P2ParcelableSpan {

    public static final int TYPE = 1;

    @Override
    public int getType() {
        return TYPE;
    }

    public static final Creator<AbsoluteSizeSpan> CREATOR  = P2ParcelableCreator.get(AbsoluteSizeSpan.class);

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
        P2ParcelUtils.writeType(dest,this);
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
}