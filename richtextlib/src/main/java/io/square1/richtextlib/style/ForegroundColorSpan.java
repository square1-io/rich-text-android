package io.square1.richtextlib.style;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

public class ForegroundColorSpan extends CharacterStyle implements UpdateAppearance, P2ParcelableSpan {

    public static final Creator<ForegroundColorSpan> CREATOR  = P2ParcelableCreator.get(ForegroundColorSpan.class);

    public static final int TYPE = 3;

    @Override
    public int getType() {
        return TYPE;
    }

    private  int mColor;

    public ForegroundColorSpan(int color) {
        mColor = color;
    }

    public ForegroundColorSpan(){}

    public ForegroundColorSpan(Parcel src) {
        readFromParcel(src);
    }
    

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        P2ParcelUtils.writeType(dest,this);
        dest.writeInt(mColor);

    }

    public int getForegroundColor() {
        return mColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mColor);
    }

    @Override
    public void readFromParcel(Parcel src) {
        mColor = src.readInt();
    }
}