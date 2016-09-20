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

package io.square1.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;

import io.square1.richtextlib.util.Utils;

/**
 * Created by roberto on 20/09/2016.
 */

public class ParcelableCreator <T extends DynamicParcelable> implements Parcelable.Creator<T> {

    private Class<T> mClass;

    public ParcelableCreator(Class<T> tClass){
        mClass = tClass;
    }

    @Override
    public T createFromParcel(Parcel source) {
        T newInstance = Utils.newInstance(mClass);
        newInstance.readFromParcel(source);
        return newInstance;
    }

    @Override
    public T[] newArray(int size) {
        return (T[]) Array.newInstance(mClass, size);
    }
}
