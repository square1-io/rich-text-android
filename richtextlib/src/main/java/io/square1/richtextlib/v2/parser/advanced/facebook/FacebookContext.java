package io.square1.richtextlib.v2.parser.advanced.facebook;

import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.parser.handlers.AppendContentHandler;
import io.square1.richtextlib.v2.parser.handlers.IgnoreContentHandler;

/**
 * Created by roberto on 17/12/2015.
 */
public class FacebookContext extends MarkupContext {

    public enum FBPostType {
        EUnknown,
        EPost,
        EVideo
    }


    private String mFacebookPostUrl;
    private FBPostType mType;

    public FacebookContext(RichTextV2 richText, Style style) {
        super();
        mType = FBPostType.EUnknown;
        setRichText(richText);
        setStyle(style);
    }

    public void setType(FBPostType type){
        mType = type;
    }

    public FBPostType getType(){
        return mType;
    }


    public void setFacebookPostUrl(String postUrl){
        mFacebookPostUrl = postUrl;
    }

    public String getFacebookPostUrl(){
        return mFacebookPostUrl;
    }

//    public RichTextDocumentElement replaceBuilder(RichTextDocumentElement in){
//        if(mInternalContent == null){
//            mInternalContent = new RichTextDocumentElement();
//        }
//        return mInternalContent;
//    }


    @Override
    public TagHandler getTagHandler(MarkupTag tag) {

        TagHandler handler = tag.getTagHandler();

        if(handler == null) {
            if("a".equalsIgnoreCase(tag.tag) == false) {
                handler = new AppendContentHandler();
                handler.replaceContext(this);
                tag.setTagHandler(handler);
            }else {
                return super.getTagHandler(tag);
            }
        }

        return handler;
    }

}
