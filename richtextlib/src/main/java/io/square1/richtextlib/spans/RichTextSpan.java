package io.square1.richtextlib.spans;

import android.os.Parcel;
import android.os.Parcelable;

import io.square1.parcelable.DynamicParcelable;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.Utils;

/**
 * Created by roberto on 12/06/15.
 */
public interface RichTextSpan extends DynamicParcelable {

    public  static  final  Parcelable.Creator<RichTextSpan> CREATOR  = new Parcelable.Creator<RichTextSpan>() {


        @Override
        public  RichTextSpan createFromParcel(Parcel source) {
            String type = source.readString();
            RichTextSpan obj = Utils.newInstance(type);
            obj.readFromParcel(source);
            return obj;
        }

        @Override
        public RichTextSpan[] newArray(int size) {
            return new RichTextSpan[size];
        }
    };

    int getType();
   /// void setType(int type);


    void onSpannedSetToView(RichContentViewDisplay view);
    void onAttachedToView(RichContentViewDisplay view);
    void onDetachedFromView(RichContentViewDisplay view);

}
