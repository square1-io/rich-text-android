package io.square1.richtextlib.v2.parser.handlers;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

/**
 * Created by roberto on 13/12/2015.
 */
public class IgnoreContentHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

    }

    public boolean processContent() {
        return false;
    }
}
