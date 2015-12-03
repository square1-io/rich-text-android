
package io.square1.richtextlib.spans;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

/**
 * Changes the typeface family of the text to which the span is attached.
 */
public class TypefaceSpan extends MetricAffectingSpan implements RichTextSpan {

    public static final Parcelable.Creator<TypefaceSpan> CREATOR  = DynamicParcelableCreator.getInstance(TypefaceSpan.class);

    public static final int TYPE = UniqueId.getType();

    @Override
    public int getType() {
        return TYPE;
    }

    private  String mFamily;

    /**
     * @param family The font family for this typeface.  Examples include
     * "monospace", "serif", and "sans-serif".
     */
    public TypefaceSpan(String family) {
        mFamily = family;
    }

    public TypefaceSpan(Parcel src) {
        readFromParcel(src);
    }

    public TypefaceSpan(){}

    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeString(mFamily);
    }

    /**
     * Returns the font family name.
     */
    public String getFamily() {
        return mFamily;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        apply(ds, mFamily);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        apply(paint, mFamily);
    }

    private static void apply(Paint paint, String family) {
        int oldStyle;

        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        Typeface tf = Typeface.create(family, oldStyle);
        int fake = oldStyle & ~tf.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }

    @Override
    public void readFromParcel(Parcel src) {
        mFamily = src.readString();
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
