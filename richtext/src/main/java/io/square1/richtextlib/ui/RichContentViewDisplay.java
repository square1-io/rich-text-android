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

package io.square1.richtextlib.ui;

import android.content.Context;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;


import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.ClickableSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;

/**
 * Created by roberto on 24/06/15.
 */
public interface RichContentViewDisplay extends Drawable.Callback {


    Point getSpanOrigin(Object span);
    void addSubView(View view);
    void setText(RichTextDocumentElement content);
    void setUrlBitmapDownloader(UrlBitmapDownloader downloader);
    void setRichTextContentChanged(RichTextContentChanged richTextContentChanged);



    public interface OnSpanClickedObserver {
         boolean onSpanClicked(ClickableSpan span);
    }

    public interface RichTextContentChanged {
        void onContentChanged(RichContentViewDisplay view);
    }


     UrlBitmapDownloader getDownloader();

    void invalidate();
    void requestLayout();
    boolean viewAttachedToWindow();
    int getMeasuredWidth();
     void performLayout();
    Context getContext();

    void mediaSizeUpdated();

    public int getPaddingLeft();
    public int getPaddingRight();

    Appearance getStyle();

}
