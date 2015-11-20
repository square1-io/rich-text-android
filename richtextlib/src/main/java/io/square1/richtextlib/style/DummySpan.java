package io.square1.richtextlib.style;

import android.os.Parcel;
import android.os.Parcelable;

import io.square1.richtextlib.ui.RichTextView;

public class DummySpan implements P2ParcelableSpan {
        
        public static final Parcelable.Creator<DummySpan> CREATOR = new Creator<DummySpan>() {
            @Override
            public DummySpan createFromParcel(Parcel source) {
                return new DummySpan();
            }

            @Override
            public DummySpan[] newArray(int size) {
                return new DummySpan[0];
            }
        };

        @Override
        public int getType() {
            return -2000;
        }

        @Override
        public void readFromParcel(Parcel src) {

        }

        @Override
        public void onSpannedSetToView(RichTextView view) {

        }

        @Override
        public void onAttachedToView(RichTextView view) {

        }

        @Override
        public void onDetachedFromView(RichTextView view) {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }