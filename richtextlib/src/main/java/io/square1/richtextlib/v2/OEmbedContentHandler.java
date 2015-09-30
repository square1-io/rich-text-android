package io.square1.richtextlib.v2;

import io.square1.richtextlib.EmbedUtils;

/**
 * Created by roberto on 10/09/15.
 */
public class OEmbedContentHandler extends ContentItem {

    private String mBaseUrl;
    private String mId;
    private EmbedUtils.TEmbedType mType;

    public OEmbedContentHandler(String url, String id, EmbedUtils.TEmbedType type){
        mBaseUrl = url;
        mId = id;
        mType = type;
    }

    public EmbedUtils.TEmbedType getType(){
        return mType;
    }

    public static OEmbedContentHandler newInstance(EmbedUtils.TEmbedType type, String content){
       return new OEmbedContentHandler(content, content, type);
    }

    public String getContent() {
        return mId;
    }
}
