package io.square1.richtextlib.v2.parser.handlers;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public abstract class BaseListHandler extends TagHandler {

    private static int sNestedListsCount = 0;

    public static int getNestedListsCount(){
        return sNestedListsCount;
    }

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 1);
        sNestedListsCount ++;
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out,1);
        sNestedListsCount --;

    }


    @Override
    public boolean closeWhenSplitting(){
        return false;
    }


    @Override
    public boolean openWhenSplitting(){
        return false;
    }
}
