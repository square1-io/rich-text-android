package io.square1.richtextlib.v2.parser.advanced.twitter;

import android.text.TextUtils;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

class TwitterLinkTagHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        String href = tag.attributes.getValue("", "href");
        String tweetId = EmbedUtils.getTweetId(href);
        if (TextUtils.isEmpty(tweetId) == false) {
            ((TwitterContext) context).setTweetId(tweetId);
            //context.getRichText().onEmbedFound(EmbedUtils.TEmbedType.ETwitter,tweetId);
        }
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {


    }

    @Override
    public boolean closeWhenSplitting() {
        return false;
    }

    @Override
    public boolean openWhenSplitting() {
        return false;
    }

    public boolean processContent() {
        return false;
    }
}