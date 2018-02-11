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
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

import io.square1.parcelable.DynamicParcelableCreator;

/**
 * Created by roberto on 04/10/15.
 */
public class RichDocument implements Parcelable {

    public static final RichDocument EMPTY = new RichDocument("", new ArrayList());

    private ArrayList<DocumentElement> mElements;
    private String mTitle;

    public RichDocument(String title, ArrayList items){
        super();
        mElements = items;
        mTitle = title;
    }





    public ArrayList<DocumentElement> getElements(){
        return mElements;
    }

    public String getTitle(){
        return mTitle;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mElements);
        dest.writeString(this.mTitle);
    }

    protected RichDocument(Parcel in) {
        this.mElements = in.createTypedArrayList(DocumentElement.CREATOR);
        this.mTitle = in.readString();
    }

    public static final Creator<RichDocument> CREATOR = new Creator<RichDocument>() {
        @Override
        public RichDocument createFromParcel(Parcel source) {
            return new RichDocument(source);
        }

        @Override
        public RichDocument[] newArray(int size) {
            return new RichDocument[size];
        }
    };


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RichDocument that = (RichDocument) o;

        if(mElements.size() != that.mElements.size()){
            return false;
        }

        for(int index = 0; index < mElements.size(); index ++) {

            DocumentElement d1 = mElements.get(index);
            DocumentElement d2 = that.mElements.get(index);

            if(d1.equals(d2) == false){
                return false;
            }

        }

        if( mTitle.equals(that.mTitle) == false ){
            Log.d("DOC" , "different title");
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mElements.hashCode();
        result = 31 * result + mTitle.hashCode();
        return result;
    }
}
