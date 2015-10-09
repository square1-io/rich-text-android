package io.square1.richtextlib.v2.parser.handlers;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;

/**
 * Created by roberto on 04/09/15.
 */
public class OLHandler extends BaseListHandler {


    private int mIndex = 0;


    public int getNextIndex(){
        return ++ mIndex;
    }

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        super.onTagOpen(context,tag,out);
        mIndex = 0;

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
       super.onTagClose(context, tag, out);

    }
}
