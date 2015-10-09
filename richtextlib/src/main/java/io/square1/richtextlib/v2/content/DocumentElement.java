package io.square1.richtextlib.v2.content;

import android.os.Parcel;
import android.os.Parcelable;

import io.square1.parcelable.DynamicParcelable;
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
        dest.writeString(getClass().getName());
        write(dest, flags);
    }


    public abstract void write(Parcel dest, int flags);
    public abstract void readFromParcel(Parcel source);
}
