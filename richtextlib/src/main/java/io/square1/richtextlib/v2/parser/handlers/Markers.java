package io.square1.richtextlib.v2.parser.handlers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.square1.richtextlib.EmbedUtils;

/**
 * Created by roberto on 07/09/15.
 */
public interface Markers {

    //those are just markers classes to tag specific index in the string
      class LeadingMargin {}
      class Bold { }
      class Italic { }
      class Underline { }
      class Big { }
      class Small { }
      class Monospace { }
      class Bullet { }
      class Code {}
      class Blockquote {

        public static final String CLASS_TWEET = "twitter-tweet";

        private List<String> mClasses;

        Blockquote(List<String> elementClass){
            mClasses = elementClass;
        }

        public List<String> getElementClass(){
            return mClasses;
        }
    }
      class Super { }
      class Sub { }

      class Background {

        public  int mColor;
        public Background(int color){
            mColor = color;
        }
    }

      class Font {
        public String mColor;
        public String mFace;

        public Font(String color, String face) {
            mColor = color;
            mFace = face;
        }
    }

      class Href {

        public String mHref;
        public EmbedUtils.TEmbedType type;

        public Href(String href) {
            mHref = href;
            if( EmbedUtils.parseLink(this, href, new EmbedUtils.ParseLinkCallback() {
                @Override
                public void onLinkParsed(Object callingObject, String result, EmbedUtils.TEmbedType type) {
                    Href.this.type = type;
                    Href.this.mHref = result;

                }
            }) == false){
                type = EmbedUtils.TEmbedType.EUnsupported;
            }
        }
    }

      class Header {

        public final int level;

        public Header(int level) {
            this.level = level;
        }
    }

}
