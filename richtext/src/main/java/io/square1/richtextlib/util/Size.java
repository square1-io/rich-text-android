/*
 * Copyright (c) 2017. Roberto  Prato <https://github.com/robertoprato>
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

package io.square1.richtextlib.util;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roberto on 01/08/2017.
 */

public class Size implements Parcelable {

    @Override
    public String toString() {
        return "Size{" +
                "mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                '}';
    }

    private final int mWidth;
    private final int mHeight;

    public Size(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public static boolean valid(Size size){

        if(size == null) return false;
        if(size.mWidth == NumberUtils.INVALID) return false;
        if(size.mHeight == NumberUtils.INVALID) return false;

        return true;

    }

    public Rect bounds(int x, int y){
        return new Rect(x,y, mWidth, mHeight);
    }

    public Rect bounds(){
        return bounds(0,0);
    }

    public final int getHeight(){
        return mHeight;
    }

    public final int getWidth(){
        return mWidth;
    }

    public final double getRatio(){
        return (double)mHeight / (double)mWidth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mHeight);
    }

    protected Size(Parcel in) {
        this.mWidth = in.readInt();
        this.mHeight = in.readInt();
    }

    public static final Parcelable.Creator<Size> CREATOR = new Parcelable.Creator<Size>() {
        @Override
        public Size createFromParcel(Parcel source) {
            return new Size(source);
        }

        @Override
        public Size[] newArray(int size) {
            return new Size[size];
        }
    };
}
