package io.square1.richtextlib.style;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;


import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

public interface RichAlignmentSpan extends android.text.style.AlignmentSpan, P2ParcelableSpan {

    public Layout.Alignment getAlignment();

    public static class Standard  implements RichAlignmentSpan {

        public static final int TYPE = UniqueId.getType();

        @Override
        public int getType() {
            return TYPE;
        }

        public static final Parcelable.Creator<Standard> CREATOR  = P2ParcelableCreator.get(Standard.class);

        public Standard(Layout.Alignment align) {
            mAlignment = align;
        }

        public Standard(Parcel src) {
            readFromParcel(src);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            P2ParcelUtils.writeType(dest,this);
            dest.writeString(mAlignment.name());
        }

        public Layout.Alignment getAlignment() {
            return mAlignment;
        }

        private  Layout.Alignment mAlignment;

        @Override
        public void readFromParcel(Parcel src) {
            mAlignment = Layout.Alignment.valueOf(src.readString());
        }

        @Override
        public void onAttachedToView(RichContentViewDisplay view) {

        }

        @Override
        public void onDetachedFromView(RichContentViewDisplay view) {

        }

        @Override
        public void onSpannedSetToView(RichContentViewDisplay view){

        }
    }


}