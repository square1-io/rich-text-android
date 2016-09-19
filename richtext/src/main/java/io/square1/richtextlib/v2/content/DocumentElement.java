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

import io.square1.parcelable.DynamicParcelable;
import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.util.Utils;

/**
 * Created by roberto on 08/09/15.
 */
public abstract class DocumentElement implements DynamicParcelable {


    public DocumentElement(){}

    private DocumentElement(Parcel in){}

    public static final Creator<DocumentElement> CREATOR = new Creator<DocumentElement>() {

        @Override
        public DocumentElement createFromParcel(Parcel in) {

            String className = in.readString();
            DocumentElement item = Utils.newInstance(className);
            if(item != null){
                item.readFromParcel(in);
            }
            return item;

        }

        @Override
        public DocumentElement[] newArray(int size) {
            return new DocumentElement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(Parcel dest, int flags){
        DynamicParcelableCreator.writeType(dest, this);
        write(dest, flags);
    }


    protected abstract void write(Parcel dest, int flags);
    public abstract void readFromParcel(Parcel source);
}
