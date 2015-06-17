package io.square1.richtextlib.style;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;

/**
 * Created by roberto on 12/06/15.
 */
public class P2ParcelableCreator<T extends P2ParcelableSpan> implements Parcelable.Creator<T> {


    public static <E extends P2ParcelableSpan> P2ParcelableCreator<E> get(Class<E> tClass){
        return new P2ParcelableCreator<E>(tClass);
    }

    Class<T> mType;

    P2ParcelableCreator(Class<T> tClass){
        mType = tClass;
    }

    @Override
    public T createFromParcel(Parcel source) {

        try {

            T obj =  mType.newInstance();
            //skip type
            source.readInt();
            obj.readFromParcel(source);
            return obj;

        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public T[] newArray(int size) {
        return (T[]) Array.newInstance(mType, size);
    }
}
