
package io.square1.richtextlib.style;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

/**
 * Sets the text color, size, style, and typeface to match a TextAppearance
 * resource.
 */
public class TextAppearanceSpan extends MetricAffectingSpan implements P2ParcelableSpan {

    private  String mTypeface;
    private  int mStyle;
    private  int mTextSize;
    private  ColorStateList mTextColor;
    private  ColorStateList mTextColorLink;

    public static final Parcelable.Creator<TextAppearanceSpan> CREATOR  = DynamicParcelableCreator.getInstance(TextAppearanceSpan.class);

    public static final int TYPE = UniqueId.getType();

    @Override
    public int getType() {
        return TYPE;
    }

    TextAppearanceSpan(){}

//    /**
//     * Uses the specified TextAppearance resource to determine the
//     * text appearance.  The <code>appearance</code> should be, for example,
//     * <code>android.R.style.TextAppearance_Small</code>.
//     */
//    public TextAppearanceSpan(Context context, int appearance) {
//        this(context, appearance, -1);
//    }

    /**
//     * Uses the specified TextAppearance resource to determine the
//     * text appearance, and the specified text color resource
//     * to determine the color.  The <code>appearance</code> should be,
//     * for example, <code>android.R.style.TextAppearance_Small</code>,
//     * and the <code>colorList</code> should be, for example,
//     * <code>android.R.styleable.Theme_textColorPrimary</code>.
//     */
//    public TextAppearanceSpan(Context context, int appearance, int colorList) {
//        ColorStateList textColor;
//
//        TypedArray a =
//            context.obtainStyledAttributes(appearance,
//                                           com.android.internal.R.styleable.TextAppearance);
//
//        textColor = a.getColorStateList(com.android.internal.R.styleable.
//                                        TextAppearance_textColor);
//        mTextColorLink = a.getColorStateList(com.android.internal.R.styleable.
//                                        TextAppearance_textColorLink);
//        mTextSize = a.getDimensionPixelSize(com.android.internal.R.styleable.
//                                        TextAppearance_textSize, -1);
//
//        mStyle = a.getInt(com.android.internal.R.styleable.TextAppearance_textStyle, 0);
//        String family = a.getString(com.android.internal.R.styleable.TextAppearance_fontFamily);
//        if (family != null) {
//            mTypeface = family;
//        } else {
//            int tf = a.getInt(com.android.internal.R.styleable.TextAppearance_typeface, 0);
//
//            switch (tf) {
//                case 1:
//                    mTypeface = "sans";
//                    break;
//
//                case 2:
//                    mTypeface = "serif";
//                    break;
//
//                case 3:
//                    mTypeface = "monospace";
//                    break;
//
//                default:
//                    mTypeface = null;
//                    break;
//            }
//        }
//
//        a.recycle();
//
//        if (colorList >= 0) {
//          //  a = context.obtainStyledAttributes(com.android.R.style.Theme,
//           //                                 com.android.internal.R.styleable.Theme);
//
//         //   textColor = a.getColorStateList(colorList);
//         //   a.recycle();
//        }
//
//        mTextColor = textColor;
//    }

    /**
     * Makes text be drawn with the specified typeface, size, style,
     * and colors.
     */
    public TextAppearanceSpan(String family, int style, int size,
                              ColorStateList color, ColorStateList linkColor) {
        mTypeface = family;
        mStyle = style;
        mTextSize = size;
        mTextColor = color;
        mTextColorLink = linkColor;
    }

    public TextAppearanceSpan(Parcel src) {
        readFromParcel(src);
    }
    

    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        P2ParcelUtils.writeType(dest, this);
        dest.writeString(mTypeface);
        dest.writeInt(mStyle);
        dest.writeInt(mTextSize);
        if (mTextColor != null) {
            dest.writeInt(1);
            mTextColor.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        if (mTextColorLink != null) {
            dest.writeInt(1);
            mTextColorLink.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
    }

    /**
     * Returns the typeface family specified by this span, or <code>null</code>
     * if it does not specify one.
     */
    public String getFamily() {
        return mTypeface;
    }

    /**
     * Returns the text color specified by this span, or <code>null</code>
     * if it does not specify one.
     */
    public ColorStateList getTextColor() {
        return mTextColor;
    }

    /**
     * Returns the link color specified by this span, or <code>null</code>
     * if it does not specify one.
     */
    public ColorStateList getLinkTextColor() {
        return mTextColorLink;
    }

    /**
     * Returns the text size specified by this span, or <code>-1</code>
     * if it does not specify one.
     */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * Returns the text style specified by this span, or <code>0</code>
     * if it does not specify one.
     */
    public int getTextStyle() {
        return mStyle;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        updateMeasureState(ds);

        if (mTextColor != null) {
            ds.setColor(mTextColor.getColorForState(ds.drawableState, 0));
        }

        if (mTextColorLink != null) {
            ds.linkColor = mTextColorLink.getColorForState(ds.drawableState, 0);
        }
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        if (mTypeface != null || mStyle != 0) {
            Typeface tf = ds.getTypeface();
            int style = 0;

            if (tf != null) {
                style = tf.getStyle();
            }

            style |= mStyle;

            if (mTypeface != null) {
                tf = Typeface.create(mTypeface, style);
            } else if (tf == null) {
                tf = Typeface.defaultFromStyle(style);
            } else {
                tf = Typeface.create(tf, style);
            }

            int fake = style & ~tf.getStyle();

            if ((fake & Typeface.BOLD) != 0) {
                ds.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                ds.setTextSkewX(-0.25f);
            }

            ds.setTypeface(tf);
        }

        if (mTextSize > 0) {
            ds.setTextSize(mTextSize);
        }
    }

    @Override
    public void readFromParcel(Parcel src) {
        mTypeface = src.readString();
        mStyle = src.readInt();
        mTextSize = src.readInt();
        if (src.readInt() != 0) {
            mTextColor = ColorStateList.CREATOR.createFromParcel(src);
        } else {
            mTextColor = null;
        }
        if (src.readInt() != 0) {
            mTextColorLink = ColorStateList.CREATOR.createFromParcel(src);
        } else {
            mTextColorLink = null;
        }
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
