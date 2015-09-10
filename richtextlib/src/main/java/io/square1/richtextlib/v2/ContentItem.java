package io.square1.richtextlib.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roberto on 08/09/15.
 */
public abstract class ContentItem implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
