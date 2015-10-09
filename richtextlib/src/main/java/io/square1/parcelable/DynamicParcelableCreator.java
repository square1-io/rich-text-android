package io.square1.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;

/**
 * Created by roberto on 12/06/15.
 */
public class DynamicParcelableCreator<T extends DynamicParcelable> implements Parcelable.Creator<T> {


    public static <E extends DynamicParcelable> DynamicParcelableCreator<E> getInstance(Class<E> tClass){
        return new DynamicParcelableCreator<E>(tClass);
    }

    Class<T> mType;

    DynamicParcelableCreator(Class<T> tClass){
        mType = tClass;
    }

    @Override
    public T createFromParcel(Parcel source) {

        try {

            T obj =  mType.newInstance();
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
