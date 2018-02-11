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

public class ParcelableUtil {

    public static byte[] marshall(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        parcelable.writeToParcel(parcel,0);
        byte[] bytes = parcel.marshall();
        parcel.recycle(); // not sure if needed or a good idea
        return bytes;
    }

    public static Parcel unMarshall(byte[] bytes) {
        Parcel p = Parcel.obtain();
        p.setDataPosition(0);
        p.unmarshall(bytes,0,bytes.length);
        p.setDataPosition(0);
        return p;
    }

    public static <T extends Parcelable> T unMarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unMarshall(bytes);
        return creator.createFromParcel(parcel);
    }
}