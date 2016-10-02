/*
 * Copyright (c) 2015. Roberto  Prato <https://github.com/robertoprato>
 *
 *  *
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package io.square1.richtextlib.spans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.view.View;


import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

public interface RichAlignmentSpan extends android.text.style.AlignmentSpan, RichTextSpan {

    public Layout.Alignment getAlignment();

    public static class Standard  implements RichAlignmentSpan {

        public static final int TYPE = UniqueId.getType();

        @Override
        public int getType() {
            return TYPE;
        }

        public static final Parcelable.Creator<io.square1.richtextlib.spans.RichAlignmentSpan.Standard> CREATOR  = DynamicParcelableCreator.getInstance(io.square1.richtextlib.spans.RichAlignmentSpan.Standard.class);

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
            DynamicParcelableCreator.writeType(dest, this);
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
        public void onAttachedToWindow(RichContentViewDisplay view) {

        }

        @Override
        public void onDetachedFromWindow(RichContentViewDisplay view) {

        }

        @Override
        public void onSpannedSetToView(RichContentView view){

        }

        @Override
        public void onViewAttachedToWindow(View v) {

        }

        @Override
        public void onViewDetachedFromWindow(View v) {

        }
    }


}