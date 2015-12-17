package io.square1.richtextlib.v2.parser.advanced.twitter;

import android.text.TextUtils;

import io.square1.richtextlib.EmbedUtils;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;


public  class TwitterQuoteTagHandler extends TagHandler {


        private TwitterContext mTwitterContext;

        @Override
        public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

            if(TextUtils.isEmpty(mTwitterContext.getTweetId()) == false){

                context.getRichText().onEmbedFound(EmbedUtils.TEmbedType.ETwitter,
                        mTwitterContext.getTweetId());

            }
        }

        @Override
        public MarkupContext getReplacementContext(){

            if(mTwitterContext == null){

                mTwitterContext = new TwitterContext(getInitialContext().getRichText(),
                        getInitialContext().getStyle());
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