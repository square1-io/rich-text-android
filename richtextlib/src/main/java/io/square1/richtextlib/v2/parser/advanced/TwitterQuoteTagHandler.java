package io.square1.richtextlib.v2.parser.advanced;

import android.text.TextUtils;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.parser.handlers.BLOCKQUOTEHandler;
import io.square1.richtextlib.v2.parser.handlers.IgnoreContentHandler;

/**
 * Created by roberto on 13/12/2015.
 */
public class TwitterQuoteTagHandler extends TagHandler {



    public static class TwitterContext extends MarkupContext {

        private String mTweetId;

        public void setTweetId(String tweetId){
            mTweetId = tweetId;
        }

        public TwitterContext(RichTextV2 richText, Style style) {
            super();
            setRichText(richText);
            setStyle(style);
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


    public static class TwitterLinkTagHandler extends TagHandler {

        @Override
        public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
            String href = tag.attributes.getValue("", "href");
            String tweetId = EmbedUtils.getTweetId(href);
            if(TextUtils.isEmpty(tweetId) == false){
                ((TwitterContext)context).setTweetId(tweetId);
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

    }

    private TwitterContext mTwitterContext;

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        if(TextUtils.isEmpty(mTwitterContext.mTweetId) == false){
            context.getRichText().onEmbedFound(EmbedUtils.TEmbedType.ETwitter,mTwitterContext.mTweetId);
        }
    }


    @Override
    public MarkupContext getReplacementContext(){

        if(mTwitterContext == null){
            mTwitterContext = new TwitterContext(getInitialContext().getRichText(),getInitialContext().getStyle());
        }
        return mTwitterContext;
    }

    public  MarkupContext getInitialContext(){
        return super.getInitialContext();
    }

    @Override
    public boolean closeWhenSplitting() {
        return false;
    }

    @Override
    public boolean openWhenSplitting() {
        return false;
    }

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

    }

    @Override
    public boolean processContent() {
        return false;
    }
}
