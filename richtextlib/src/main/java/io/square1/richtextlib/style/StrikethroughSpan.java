package io.square1.richtextlib.style;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.UniqueId;

public class StrikethroughSpan extends CharacterStyle implements UpdateAppearance , P2ParcelableSpan {

    public static final Parcelable.Creator<RelativeSizeSpan> CREATOR  = P2ParcelableCreator.get(RelativeSizeSpan.class);

    public static final int TYPE = UniqueId.getType();

    @Override
    public int getType() {
        return TYPE;
    }

    public StrikethroughSpan() {
    }
    
    public StrikethroughSpan(Parcel src) {
    }
    

    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        P2ParcelUtils.writeType(dest,this);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setStrikeThruText(true);
    }

    @Override
    public void readFromParcel(Parcel src){}

    @Override
    public void onAttachedToView(RichTextView view) {

    }

    @Override
    public void onDetachedFromView(RichTextView view) {

    }

    @Override
    public void onSpannedSetToView(RichTextView view){

    }
}