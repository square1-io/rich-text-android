package io.square1.richtextlib.v2.parser.handlers;

import android.graphics.Typeface;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.RelativeSizeSpan;
import io.square1.richtextlib.style.StyleSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class BIGHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {
        SpannedBuilderUtils.startSpan(out, new Markers.Big());
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {
        SpannedBuilderUtils.endSpan(out, Markers.Big.class, new RelativeSizeSpan(1.25f));

    }
}
