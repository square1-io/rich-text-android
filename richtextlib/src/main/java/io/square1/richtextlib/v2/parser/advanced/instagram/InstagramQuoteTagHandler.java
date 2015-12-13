package io.square1.richtextlib.v2.parser.advanced.instagram;

import android.text.TextUtils;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;


public  class InstagramQuoteTagHandler extends TagHandler {


        private InstagramContext mInstagramContext;

        @Override
        public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

            if(TextUtils.isEmpty(mInstagramContext.getInstagramId()) == false){

                context.getRichText().onEmbedFound(EmbedUtils.TEmbedType.EInstagram,
                        mInstagramContext.getInstagramId());

            }
        }


        @Override
        public MarkupContext getReplacementContext(){

            if(mInstagramContext == null){

                mInstagramContext = new InstagramContext(getInitialContext().getRichText(),
                        getInitialContext().getStyle());
            }
            return mInstagramContext;
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