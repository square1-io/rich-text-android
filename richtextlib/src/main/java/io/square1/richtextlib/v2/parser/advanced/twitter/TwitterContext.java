package io.square1.richtextlib.v2.parser.advanced.twitter;

import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.parser.handlers.IgnoreContentHandler;

class TwitterContext extends MarkupContext {

    private String mTweetId;

    public void setTweetId(String tweetId){
        mTweetId = tweetId;
    }

    public TwitterContext(RichTextV2 richText, Style style) {
        super();
        setRichText(richText);
        setStyle(style);
    }

    public String getTweetId(){
        return mTweetId;
    }

    @Override
    public TagHandler getTagHandler(MarkupTag tag) {

        TagHandler handler = tag.getTagHandler();

        if(handler == null) {

            if ("a".equalsIgnoreCase(tag.tag) == true) {
                handler = new TwitterLinkTagHandler();
            } else {
                handler = new IgnoreContentHandler();
            }

            handler.replaceContext(this);
            tag.setTagHandler(handler);
        }

        return handler;
    }
}
