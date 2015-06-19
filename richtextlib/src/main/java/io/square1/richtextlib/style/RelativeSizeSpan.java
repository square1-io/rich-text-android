package io.square1.richtextlib.style;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class RelativeSizeSpan extends MetricAffectingSpan implements P2ParcelableSpan {

    public static final Parcelable.Creator<RelativeSizeSpan> CREATOR  = P2ParcelableCreator.get(RelativeSizeSpan.class);

    public static final int TYPE = 5;

    @Override
    public int getType() {
        return TYPE;
    }

    private  float mProportion;

    public RelativeSizeSpan(){

    }

    public RelativeSizeSpan(float proportion) {
        mProportion = proportion;
    }

    public RelativeSizeSpan(Parcel src) {
      readFromParcel(src);
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        P2ParcelUtils.writeType(dest,this);
        dest.writeFloat(mProportion);
    }

    public float getSizeChange() {
        return mProportion;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setTextSize(ds.getTextSize() * mProportion);
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        ds.setTextSize(ds.getTextSize() * mProportion);
    }


    @Override
    public void readFromParcel(Parcel src) {
        mProportion = src.readFloat();
    }
}