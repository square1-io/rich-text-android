package io.square1.richtextlib.v2.parser.handlers;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 19/08/15.
 */
public class PHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        //spare the new lines if starting at top
        if(out.length() > 0) {
            SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);
        }else {
            ///just start with a new line should be fine
            out.append('\n');
        }
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out,2);
    }

    //don't add extra new lines when an OEMbed was found and the page continues
    public boolean openWhenSplitting(){
        return false;
    }




}
