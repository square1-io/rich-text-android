package io.square1.richtextlib.spans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

public class BackgroundColorSpan extends CharacterStyle implements UpdateAppearance, RichTextSpan {

    public static final int TYPE = UniqueId.getType();

    private int mColor;

    public static final Parcelable.Creator<BackgroundColorSpan> CREATOR  = DynamicParcelableCreator.getInstance(BackgroundColorSpan.class);

    public BackgroundColorSpan(int color) {
        mColor = color;
    }

    public BackgroundColorSpan(Parcel src) {
        readFromParcel(src);
    }

    public BackgroundColorSpan(){}

    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeInt(mColor);
    }

    public int getBackgroundColor() {
        return mColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.bgColor = mColor;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mColor = src.readInt();
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