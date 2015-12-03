package qs.com.myapplication.spans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roberto on 03/12/2015.
 */
public interface RichSpan extends Parcelable {

     void writeToParcel(Parcel dest, int flags);
     void readFromParcel(Parcel input);
}

