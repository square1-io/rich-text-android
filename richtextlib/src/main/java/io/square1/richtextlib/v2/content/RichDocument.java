package io.square1.richtextlib.v2.content;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by roberto on 04/10/15.
 */
public class RichDocument implements Parcelable {

    public static final RichDocument EMPTY = new RichDocument("", new ArrayList());

    private ArrayList<DocumentElement> mElements;
    private String mTitle;

    public RichDocument(String title, ArrayList items){
        mElements = items;
        mTitle = title;
    }

    protected RichDocument(Parcel in) {
        mElements = in.createTypedArrayList(DocumentElement.CREATOR);
        mTitle = in.readString();
    }

    public static final Creator<RichDocument> CREATOR = new Creator<RichDocument>() {
        @Override
        public RichDocument createFromParcel(Parcel in) {
            return new RichDocument(in);
        }

        @Override
        public RichDocument[] newArray(int size) {
            return new RichDocument[size];
        }
    };

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
        dest.writeTypedList(mElements);
        dest.writeString(mTitle);
    }
}
