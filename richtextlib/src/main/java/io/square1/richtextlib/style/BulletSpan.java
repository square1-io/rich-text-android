package io.square1.richtextlib.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

public class BulletSpan implements LeadingMarginSpan, P2ParcelableSpan {

    public static final Creator<BulletSpan> CREATOR  = P2ParcelableCreator.get(BulletSpan.class);

    public static final int TYPE = 15;

    private  int mGapWidth;
    private  boolean mWantColor;
    private  int mColor;

    private static final int BULLET_RADIUS = 3;
    private static Path sBulletPath = null;
    public static final int STANDARD_GAP_WIDTH = 2;

    public BulletSpan() {
        mGapWidth = STANDARD_GAP_WIDTH;
        mWantColor = false;
        mColor = 0;
    }

    public BulletSpan(int gapWidth) {
        mGapWidth = gapWidth;
        mWantColor = false;
        mColor = 0;
    }

    public BulletSpan(int gapWidth, int color) {
        mGapWidth = gapWidth;
        mWantColor = true;
        mColor = color;
    }

    public BulletSpan(Parcel src) {
        readFromParcel(src);
    }

    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mGapWidth = src.readInt();
        mWantColor = src.readInt() != 0;
        mColor = src.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        P2ParcelUtils.writeType(dest,this);
        dest.writeInt(mGapWidth);
        dest.writeInt(mWantColor ? 1 : 0);
        dest.writeInt(mColor);
    }

    public int getLeadingMargin(boolean first) {
        return 2 * BULLET_RADIUS + mGapWidth;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {

        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();
            int oldcolor = 0;

            if (mWantColor) {
                oldcolor = p.getColor();
                p.setColor(mColor);
            }

            p.setStyle(Paint.Style.FILL);

            if (c.isHardwareAccelerated()) {
                if (sBulletPath == null) {
                    sBulletPath = new Path();
                    // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                    sBulletPath.addCircle(0.0f, 0.0f, 1.2f * BULLET_RADIUS, Path.Direction.CW);
                }

                c.save();
                c.translate(x + dir * BULLET_RADIUS, (top + bottom) / 2.0f);
                c.drawPath(sBulletPath, p);
                c.restore();
            } else {
                c.drawCircle(x + dir * BULLET_RADIUS, (top + bottom) / 2.0f, BULLET_RADIUS, p);
            }

            if (mWantColor) {
                p.setColor(oldcolor);
            }

            p.setStyle(style);
        }
    }
}