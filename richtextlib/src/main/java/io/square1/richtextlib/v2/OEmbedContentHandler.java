package io.square1.richtextlib.v2;

/**
 * Created by roberto on 10/09/15.
 */
public class OEmbedContentHandler extends ContentItem {

    private String mBaseUrl;
    private String mId;
    private String mProvider;

    public OEmbedContentHandler(String url, String id, String provider){
        mBaseUrl = url;
        mId = id;
        mProvider = provider;
    }
}
