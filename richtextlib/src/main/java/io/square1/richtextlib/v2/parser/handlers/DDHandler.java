package io.square1.richtextlib.v2.parser.handlers;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.style.LeadingMarginSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 19/08/15.
 */
public class DDHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out,1);
        SpannedBuilderUtils.startSpan(out, new Markers.LeadingMargin());
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out,1);
        SpannedBuilderUtils.endSpan(out, Markers.LeadingMargin.class, new LeadingMarginSpan(10,10));
    }


}
