package io.square1.richtextlib.v2.parser.handlers;

import android.text.TextUtils;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

/**
 * Created by roberto on 04/09/15.
 */
public class SOUNDCLOUDHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        String src = tag.attributes.getValue("", "url");
        src = EmbedUtils.getSoundCloudStream(src,"1f6456941b1176c22d44fb16ec2015a2");
        // mSpannableStringBuilder.append("\uFFFC");
        if(TextUtils.isEmpty(src) == false) {
            context.getRichText().onEmbedFound(EmbedUtils.TEmbedType.ESoundCloud, src);
        }

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

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
