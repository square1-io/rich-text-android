package io.square1.richtextlib.v2.parser.advanced.instagram;

import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.parser.handlers.IgnoreContentHandler;

class InstagramContext extends MarkupContext {

    private String mInstagramId;

    public void setInstagramId(String instagramId){
        mInstagramId = instagramId;
    }

    public InstagramContext(RichTextV2 richText, Style style) {
        super();
        setRichText(richText);
        setStyle(style);
    }

    public String getInstagramId(){
        return mInstagramId;
    }

    @Override
    public TagHandler getTagHandler(MarkupTag tag) {

        TagHandler handler = tag.getTagHandler();

        if(handler == null) {

            if ("a".equalsIgnoreCase(tag.tag) == true) {
                handler = new InstagramLinkTagHandler();
            } else {
                handler = new IgnoreContentHandler();
            }

            handler.replaceContext(this);
            tag.setTagHandler(handler);
        }

        return handler;
    }
}
