package io.square1.richtextlib.v2.parser.handlers;

import android.graphics.Typeface;
import android.text.Spannable;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.ForegroundColorSpan;
import io.square1.richtextlib.style.RelativeSizeSpan;
import io.square1.richtextlib.style.StyleSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public abstract class HeaderBaseHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

        String tagName = tag.tag;
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);
        char value =  tagName.charAt(1);
        SpannedBuilderUtils.startSpan(out, new Markers.Header( value - '1'));

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

        int len = out.length();
        Markers.Header header = out.getLastSpan(Markers.Header.class);

        int where = out.getSpanStart(header);
        out.removeSpan(header);

        // Back off not to change only the text, not the blank line.
        while (len > where && out.charAt(len - 1) == '\n') {
            len--;
        }

        if (where != len) {

            int foreground = context.getStyle().headerColor();

            out.setSpan(new ForegroundColorSpan(foreground),
                    where,
                    len,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            out.setSpan(new RelativeSizeSpan(context.getStyle().headerIncrease(header.level)),
                    where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            out.setSpan(new StyleSpan(Typeface.BOLD),
                    where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }
}
