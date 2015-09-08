package io.square1.richtextlib.v2.parser.markers;

import io.square1.richtextlib.EmbedUtils;

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