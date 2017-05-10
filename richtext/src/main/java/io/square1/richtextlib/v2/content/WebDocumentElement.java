/*
 * Copyright (c) 2017. Roberto  Prato <https://github.com/robertoprato>
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

/**
 * Created by roberto on 10/09/15.
 */
public class WebDocumentElement extends DocumentElement {

    public enum ContentType {
        EUrl,
        EHtml
    }


    private String mContent;
    private ContentType mType;

    private int mWidth = -1;
    private int mHeight  = -1;

    public WebDocumentElement(){
     super();
    }

    public WebDocumentElement(String content,
                              ContentType type,
                              int width,
                              int height){
        super();
        mType = type;
        mWidth = width;
        mHeight = height;
        mContent = content;



    }

    public ContentType getType(){
        return mType;
    }
    public String getContent(){
        return mContent;
    }


    @Override
    public void readFromParcel(Parcel source) {
        mContent = source.readString();
        int tmpMType = source.readInt();
        this.mType = tmpMType == -1 ? null : ContentType.values()[tmpMType];
        mWidth = source.readInt();
        mHeight = source.readInt();
    }

    public int getWidth(){
        return mWidth;
    }

    public int getHeight(){
        return mHeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void write(Parcel dest, int flags) {
        dest.writeString(this.mContent);
        dest.writeInt(this.mType == null ? -1 : this.mType.ordinal());
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mHeight);
    }




    public static final Creator<DocumentElement> CREATOR = DocumentElement.CREATOR;


}
