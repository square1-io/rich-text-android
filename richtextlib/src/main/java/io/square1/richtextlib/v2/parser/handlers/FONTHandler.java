package io.square1.richtextlib.v2.parser.handlers;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spannable;
import android.text.TextUtils;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.ForegroundColorSpan;
import io.square1.richtextlib.style.TextAppearanceSpan;
import io.square1.richtextlib.style.TypefaceSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

/**
 * Created by roberto on 04/09/15.
 */
public class FONTHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

        String color = tag.attributes.getValue("", "color");
        String face = tag.attributes.getValue("", "face");
        int len = out.length();
        out.setSpan(new Markers.Font(color, face), len, len, Spannable.SPAN_MARK_MARK);

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

        int len = out.length();
        Markers.Font f = out.getLastSpan(Markers.Font.class);
        int where = out.getSpanStart(f);

        out.removeSpan(f);

        if (where != len) {

            if (!TextUtils.isEmpty(f.mColor)) {
                if (f.mColor.startsWith("@")) {
                    Resources res = Resources.getSystem();
                    String name = f.mColor.substring(1);
                    int colorRes = res.getIdentifier(name, "color", "android");
                    if (colorRes != 0) {
                        ColorStateList colors = res.getColorStateList(colorRes);
                        out.setSpan(new TextAppearanceSpan(null, 0, 0, colors, null),
                                where, len,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else {
                    int c = Color.parseColor(f.mColor);
                    if (c != -1) {
                        out.setSpan(new ForegroundColorSpan(c | 0xFF000000),
                                where, len,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            if (f.mFace != null) {
                out.setSpan(new TypefaceSpan(f.mFace), where, len,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }


    }
}
