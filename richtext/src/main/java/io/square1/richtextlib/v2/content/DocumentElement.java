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

import io.square1.parcelable.DynamicParcelable;
import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.util.Utils;

/**
 * Created by roberto on 08/09/15.
 */
public abstract class DocumentElement implements Parcelable /**implements DynamicParcelable**/ {


    public DocumentElement(){}


    protected abstract void write(Parcel dest, int flags);
    public abstract void readFromParcel(Parcel source);


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    final public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getClass().getName());
        write(dest, flags);
    }

    private DocumentElement(Parcel in) {
    }

    public static final Creator<DocumentElement> CREATOR = new Creator<DocumentElement>() {
        @Override
        public DocumentElement createFromParcel(Parcel source) {

            String className = source.readString();
            DocumentElement item = Utils.newInstance(className);
            if(item != null){
               item.readFromParcel(source);
            }
            return item;

        }

        @Override
        public DocumentElement[] newArray(int size) {
            return new DocumentElement[size];
        }
    };
}
