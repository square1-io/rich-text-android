package io.square1.richtextlib.v2.content;

import android.os.Parcel;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.EmbedUtils;

/**
 * Created by roberto on 10/09/15.
 */
public class OembedDocumentElement extends DocumentElement {

    public static final Creator<OembedDocumentElement> CREATOR  = DynamicParcelableCreator.getInstance(OembedDocumentElement.class);

    private String mBaseUrl;
    private String mId;
    private EmbedUtils.TEmbedType mType;

    public OembedDocumentElement(){
     super();
    }

    public OembedDocumentElement(String url, String id, EmbedUtils.TEmbedType type){
        super();
        mBaseUrl = url;
        mId = id;
        mType = type;
    }

    public EmbedUtils.TEmbedType getType(){
        return mType;
    }

    public static OembedDocumentElement newInstance(EmbedUtils.TEmbedType type, String content){
       return new OembedDocumentElement(content, content, type);
    }

    public String getContent() {
        return mId;
    }

    @Override
    public void write(Parcel dest, int flags) {
        dest.writeString(mBaseUrl);
        dest.writeString(mId);
        dest.writeString(mType.name());
    }

    @Override
    public void readFromParcel(Parcel source) {
        mBaseUrl = source.readString();
        mId = source.readString();
        mType = EmbedUtils.TEmbedType.valueOf(source.readString());

    }
}
