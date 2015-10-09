package io.square1.richtextlib.v2.parser.handlers;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class LIHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out,1);
        MarkupTag parent = context.getParent(tag,BaseListHandler.class);

        int nestedListsCount = BaseListHandler.getNestedListsCount();

        for (int index = 0; index < nestedListsCount; index ++) {
            out.append(SpannedBuilderUtils.TAB);
        }

        TagHandler parentHandler = parent.getTagHandler();


        if(parentHandler instanceof OLHandler){
            out.append( String.valueOf (((OLHandler)parentHandler).getNextIndex()));
        }else{
            out.append(SpannedBuilderUtils.BULLET);
        }

        out.append(SpannedBuilderUtils.SPACE);
        out.append(SpannedBuilderUtils.SPACE);
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out,1);
    }


}
