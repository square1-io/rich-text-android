/*
 * Copyright (c) 2015. Roberto  Prato <https://github.com/robertoprato>
 *
 *  *
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

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
    public static class LeadingMargin {}
    public static  class Strike {}
    public static  class Bold { }
    public static  class Italic { }
    public static class Underline { }
      class Big { }
      class Small { }
      class Monospace { }
      class Bullet { }
      class Code {}
      class Sup {}

    public static class Blockquote {

        public static final String CLASS_TWEET = "twitter-tweet";

        private List<String> mClasses;

        public Blockquote(List<String> elementClass){
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

    class P{

        int newLinesAtStart;
    }

      class Header {

        public final int level;

        public Header(int level) {
            this.level = level;
        }
    }

    public class Alignment {
    }

    public abstract class MarkerWithSource {
        public String src;
        public String type;
    }

    public class Audio extends MarkerWithSource {

    }

    public class Video extends MarkerWithSource {

    }
}
