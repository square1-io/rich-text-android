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

package io.square1.richtext.io.square1.richtext.sample;

import android.content.Context;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import io.square1.richtextlib.spans.RemoteBitmapSpan;

public class GlideTarget extends SimpleTarget<GlideDrawable> {

    private RemoteBitmapSpan mSpan;
    private Context mApplicationContext;

    public GlideTarget(Context context, RemoteBitmapSpan span){
        this.mSpan = span;
        mApplicationContext = context.getApplicationContext();
    }

    @Override
    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
        mSpan.updateBitmap(mApplicationContext, resource);
    }
}