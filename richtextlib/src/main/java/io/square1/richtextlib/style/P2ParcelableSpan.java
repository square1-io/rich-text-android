package io.square1.richtextlib.style;

import android.os.Parcel;
import android.os.Parcelable;

import io.square1.richtextlib.ui.RichContentViewDisplay;

/**
 * Created by roberto on 12/06/15.
 */
public interface P2ParcelableSpan extends Parcelable {



    int getType();
   /// void setType(int type);
    void readFromParcel(Parcel src);

    void onSpannedSetToView(RichContentViewDisplay view);
    void onAttachedToView(RichContentViewDisplay view);
    void onDetachedFromView(RichContentViewDisplay view);

}
