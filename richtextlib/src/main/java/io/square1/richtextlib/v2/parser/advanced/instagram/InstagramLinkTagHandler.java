package io.square1.richtextlib.v2.parser.advanced.instagram;

import android.text.TextUtils;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

class InstagramLinkTagHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        String href = tag.attributes.getValue("", "href");
        String instagramId = EmbedUtils.getInstagramId(href);
        if (TextUtils.isEmpty(instagramId) == false) {
            ((InstagramContext) context).setInstagramId(instagramId);
            //context.getRichText().onEmbedFound(EmbedUtils.TEmbedType.ETwitter,tweetId);
        }
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {


    }

    public boolean processContent() {
        return false;
    }

    @Override
    public boolean closeWhenSplitting() {
        return false;
    }

    @Override
    public boolean openWhenSplitting() {
        return false;
    }

}