package io.square1.richtextlib.v2.parser.handlers;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class OLHandler extends BaseListHandler {


    private int mIndex = 0;


    public int getNextIndex(){
        return ++ mIndex;
    }

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {
        super.onTagOpen(context,tag,out);
        mIndex = 0;

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {
       super.onTagClose(context, tag, out);

    }
}
