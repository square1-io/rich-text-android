package io.square1.richtextlib.v2.parser.advanced;


import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.parser.advanced.instagram.InstagramQuoteTagHandler;
import io.square1.richtextlib.v2.parser.advanced.twitter.TwitterQuoteTagHandler;


/**
 * Created by roberto on 13/12/2015.
 *
 * Etends the basic context and performs extracting of
 * tweets and instagrams tags from embedded quotes tags
 */
public class AdvancedMarkupContext extends MarkupContext {


    public static final String BLOCKQUOTE_CLASS_TWEET = "twitter-tweet";
    public static final String BLOCKQUOTE_CLASS_INSTAGRAM = "instagram-media";



    public AdvancedMarkupContext() {
        super();

    }


    @Override
    public TagHandler getTagHandler(MarkupTag tag) {

        if("blockquote".equalsIgnoreCase(tag.tag)){

            String elementClass = tag.attributes.getValue("class");

            if(BLOCKQUOTE_CLASS_TWEET.equalsIgnoreCase(elementClass) == true){
                TwitterQuoteTagHandler handler = new TwitterQuoteTagHandler();
                handler.replaceContext(this);
                tag.setTagHandler(handler);
                return handler;
            }
            else if(BLOCKQUOTE_CLASS_INSTAGRAM.equalsIgnoreCase(elementClass) == true){
                InstagramQuoteTagHandler handler = new InstagramQuoteTagHandler();
                handler.replaceContext(this);
                tag.setTagHandler(handler);
                return handler;
            }
        }

        return super.getTagHandler(tag);
    }

}
