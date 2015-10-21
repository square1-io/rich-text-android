package io.square1.richtextlib.v2.parser.handlers;

import android.util.Log;

import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

/**
 * Created by roberto on 19/08/15.
 */
public final class DefaultHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        Log.i(RichTextV2.TAG, tag.tag  + " not supported" );
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

    }
}
