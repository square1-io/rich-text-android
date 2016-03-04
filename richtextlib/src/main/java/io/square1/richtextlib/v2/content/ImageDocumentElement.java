/*
 * Copyright (c) 2016. Roberto  Prato <https://github.com/robertoprato>
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

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.EmbedUtils;

/**
 * Created by roberto on 10/09/15.
 */
public class ImageDocumentElement extends DocumentElement {

    public static final Creator<ImageDocumentElement> CREATOR  = DynamicParcelableCreator.getInstance(ImageDocumentElement.class);

    private String mImageUrl;
    private Uri mClickAction;

    private int mWidth = -1;
    private int mHeight  = -1;

    public ImageDocumentElement(){
     super();
    }

    public ImageDocumentElement(String imageUrl,
                                String clickAction,
                                int width,
                                int height){
        super();


        mWidth = width;
        mHeight = height;

        mImageUrl = imageUrl;

        try {
            mClickAction = Uri.parse(clickAction);
        }catch (Exception e){
            mClickAction = Uri.EMPTY;
        }


    }

    public Uri getClickAction(){
        return mClickAction;
    }

    public String getImageURL(){
        return mImageUrl;
    }

    public static ImageDocumentElement newInstance(String imageUrl ,
                                                   String clickAction,
                                                   int width,
                                                   int height){
       return new ImageDocumentElement(imageUrl, clickAction, width, height);
    }



    @Override
    public void write(Parcel dest, int flags) {
        dest.writeString(mImageUrl);
        dest.writeParcelable(mClickAction, flags);
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);

    }

    @Override
    public void readFromParcel(Parcel source) {
        mImageUrl = source.readString();
        mClickAction = source.readParcelable(Uri.class.getClassLoader());
        mWidth = source.readInt();
        mHeight = source.readInt();
    }
}
