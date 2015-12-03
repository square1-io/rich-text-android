package io.square1.parcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Array;

import io.square1.richtextlib.spans.RichTextSpan;
import io.square1.richtextlib.util.Utils;

/**
 * Created by roberto on 12/06/15.
 */
public class DynamicParcelableCreator<T extends DynamicParcelable> implements Parcelable.Creator<T> {

    public  static  final  Parcelable.Creator<DynamicParcelable> CREATOR  = new Parcelable.Creator<DynamicParcelable>() {


        @Override
        public  DynamicParcelable createFromParcel(Parcel source) {
            String type = source.readString();
            DynamicParcelable obj = Utils.newInstance(type);
            obj.readFromParcel(source);
            return obj;
        }

        @Override
        public DynamicParcelable[] newArray(int size) {
            return new DynamicParcelable[size];
        }
    };

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

    public static void writeType(Parcel in , DynamicParcelable span){
         String className = span.getClass().getName();
        Log.i("CLASS", "-> " + className);
         in.writeString(className);
    }
}
