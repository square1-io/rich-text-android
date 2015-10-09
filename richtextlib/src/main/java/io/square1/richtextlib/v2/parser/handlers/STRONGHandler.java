package io.square1.richtextlib.v2.parser.handlers;

import android.graphics.Typeface;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.style.StyleSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class STRONGHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        SpannedBuilderUtils.startSpan(out, new Markers.Bold());

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        SpannedBuilderUtils.endSpan(out, Markers.Bold.class, new StyleSpan(Typeface.BOLD));
    }
}
