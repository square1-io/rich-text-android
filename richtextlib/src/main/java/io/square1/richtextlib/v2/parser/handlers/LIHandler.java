package io.square1.richtextlib.v2.parser.handlers;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class LIHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 1);
        out.append(SpannedBuilderUtils.TAB);
        out.append(SpannedBuilderUtils.BULLET);
        out.append(SpannedBuilderUtils.SPACE);
        out.append(SpannedBuilderUtils.SPACE);
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

    }
}
