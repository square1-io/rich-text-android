package io.square1.richtextlib.style;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.style.ParagraphStyle;

public interface AlignmentSpan extends ParagraphStyle, P2ParcelableSpan {

    public Layout.Alignment getAlignment();

    public static class Standard  implements AlignmentSpan {

        public static final int TYPE = 2;

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
    }
}