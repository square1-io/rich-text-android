package io.square1.richtextlib.v2.content;

import android.os.Parcel;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.EmbedUtils;

/**
 * Created by roberto on 10/09/15.
 */
public class OembedElement extends DocumentElement {

    public static final Creator<OembedElement> CREATOR  = DynamicParcelableCreator.getInstance(OembedElement.class);

    private String mBaseUrl;
    private String mId;
    private EmbedUtils.TEmbedType mType;

    public OembedElement(){
     super();
    }

    public OembedElement(String url, String id, EmbedUtils.TEmbedType type){
        super();
        mBaseUrl = url;
        mId = id;
        mType = type;
    }

    public EmbedUtils.TEmbedType getType(){
        return mType;
    }

    public static OembedElement newInstance(EmbedUtils.TEmbedType type, String content){
       return new OembedElement(content, content, type);
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
