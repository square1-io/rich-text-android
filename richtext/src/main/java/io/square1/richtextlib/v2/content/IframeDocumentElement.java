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

import android.net.Uri;
import android.os.Parcel;

/**
 * Created by roberto on 10/09/15.
 */
public class IframeDocumentElement extends DocumentElement {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IframeDocumentElement that = (IframeDocumentElement) o;

        if (mWidth != that.mWidth) return false;
        if (mHeight != that.mHeight) return false;
        if (!mIframeURL.equals(that.mIframeURL)) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = mIframeURL.hashCode();
        result = 31 * result + mWidth;
        result = 31 * result + mHeight;
        return result;
    }

    private String mIframeURL;


    private int mWidth = -1;
    private int mHeight  = -1;

    public IframeDocumentElement(){
     super();
    }

    public IframeDocumentElement(String imageUrl,
                                 int width,
                                 int height){
        super();


        mWidth = width;
        mHeight = height;
        mIframeURL = imageUrl;



    }


    public String getIframeURL(){
        return mIframeURL;
    }

    public static IframeDocumentElement newInstance(String url ,
                                                    int width,
                                                    int height){
       return new IframeDocumentElement(url, width, height);
    }




    @Override
    public void readFromParcel(Parcel source) {
        mIframeURL = source.readString();
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
        dest.writeString(this.mIframeURL);
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mHeight);
    }



    public static final Creator<DocumentElement> CREATOR = DocumentElement.CREATOR;


}
