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

package io.square1.richtextlib.v2.content;

import android.os.Parcel;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.EmbedUtils;

/**
 * Created by roberto on 10/09/15.
 */
public class OembedDocumentElement extends DocumentElement {


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
    public void readFromParcel(Parcel source) {
        mBaseUrl = source.readString();
        mId = source.readString();
        mType = EmbedUtils.TEmbedType.valueOf(source.readString());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void write(Parcel dest, int flags) {
        dest.writeString(this.mBaseUrl);
        dest.writeString(this.mId);
        dest.writeInt(this.mType == null ? -1 : this.mType.ordinal());
    }

    protected OembedDocumentElement(Parcel in) {
        super(in);
        this.mBaseUrl = in.readString();
        this.mId = in.readString();
        int tmpMType = in.readInt();
        this.mType = tmpMType == -1 ? null : EmbedUtils.TEmbedType.values()[tmpMType];
    }

    public static final Creator<OembedDocumentElement> CREATOR = new Creator<OembedDocumentElement>() {
        @Override
        public OembedDocumentElement createFromParcel(Parcel source) {
            return new OembedDocumentElement(source);
        }

        @Override
        public OembedDocumentElement[] newArray(int size) {
            return new OembedDocumentElement[size];
        }
    };
}
