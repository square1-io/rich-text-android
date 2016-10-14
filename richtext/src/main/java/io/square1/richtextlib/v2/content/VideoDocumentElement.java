/*
 * Copyright (c) 2016. Roberto  Prato <https://github.com/robertoprato>
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

package io.square1.richtextlib.v2.content;

import android.os.Parcel;

import io.square1.richtextlib.EmbedUtils;

/**
 * Created by roberto on 10/09/15.
 */
public class VideoDocumentElement extends DocumentElement {


    private String mVideoUrl;
    private int mWidth = -1;
    private int mHeight  = -1;


    public static VideoDocumentElement newInstance(String videoUrl , int width, int height){
        return new VideoDocumentElement(videoUrl, width, height);
    }

    public VideoDocumentElement(){
     super();
    }

    @Override
    protected void write(Parcel dest, int flags) {
        dest.writeString(mVideoUrl);
    }

    @Override
    public void readFromParcel(Parcel source) {
        mVideoUrl = source.readString();
    }

    public VideoDocumentElement(String video, int width, int height){
        super();
        mVideoUrl = video;
        mWidth = width;
        mHeight = height;

    }

    public String getContent() {
        return mVideoUrl;
    }

    public int getWidth(){
        return mWidth;
    }

    public int getHeight(){
        return mHeight;
    }


    public static final Creator<DocumentElement> CREATOR = DocumentElement.CREATOR;
}
