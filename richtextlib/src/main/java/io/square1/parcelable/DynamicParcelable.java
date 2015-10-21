package io.square1.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roberto on 04/10/15.
 */
public interface DynamicParcelable extends Parcelable {


    void readFromParcel(Parcel src);
}
