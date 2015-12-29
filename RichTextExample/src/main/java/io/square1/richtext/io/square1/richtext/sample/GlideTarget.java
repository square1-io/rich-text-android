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