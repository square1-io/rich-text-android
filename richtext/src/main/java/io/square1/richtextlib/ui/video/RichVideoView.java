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

package io.square1.richtextlib.ui.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;

import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.AspectRatioFrameLayout;
import io.square1.richtextlib.util.NumberUtils;

/**
 * Created by roberto on 12/10/15.
 */
public class RichVideoView extends FrameLayout implements RichMediaPlayer.FirstFrameAvailableListener ,
        MediaPlayer.OnBufferingUpdateListener,
        RichMediaPlayer.OnCompletionListener,
        RichMediaPlayer.OnVideoSizeListener {

    public interface  RichVideoViewListener {

        public void onVideoReady(RichVideoView videoView);
        public void onVideoSizeAvailable(RichVideoView videoView);

    }

    private RichVideoViewListener mRichVideoViewListener;

    private FullScreenMediaController mFullScreenMediaController;
    private AspectRatioFrameLayout mMainVideoContainer;
    private TextureView mTextureView;
    private RichMediaPlayer mMediaPlayer;
    private SurfaceTexture mSurface;

    //private ImageView mPlayButton;
    private ProgressBar mLoadingProgress;
    private String mCurrentUri;


    public RichVideoView(Context context) {
        super(context);
        init();
    }

    public RichVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public RichVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void initMediaPlayer(){

        if(isInEditMode() == true) {
            return;
        }
        if(mMediaPlayer == null) {
            mMediaPlayer = new RichMediaPlayer(getContext());
            mMediaPlayer.setFirstFrameAvailableListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnVideoSizeListener(this);
        }

        if(mSurface != null){
            mMediaPlayer.setSurfaceTexture(mSurface);
        }

    }
    public void init(){



        initMediaPlayer();


        LayoutInflater.from(getContext())
                .inflate(R.layout.internal_rich_text_video_controller,
                this, true);

        mMainVideoContainer = (AspectRatioFrameLayout)findViewById(R.id.internal_aspect_ratio_view);

        mTextureView =  (TextureView)findViewById(R.id.internal_texture_view);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);


        if(mTextureView.isAvailable() == true){
            mSurfaceTextureListener.onSurfaceTextureAvailable(mTextureView.getSurfaceTexture(),
                    mTextureView.getWidth()
                    ,mTextureView.getHeight());
        }

        mFullScreenMediaController = new FullScreenMediaController(getContext());
        mFullScreenMediaController.setAnchorView(mMainVideoContainer);
        mFullScreenMediaController.setMediaPlayer(mMediaPlayer);

        //setup the progress bar
        mLoadingProgress = (ProgressBar) findViewById(R.id.internal_progress);
        mLoadingProgress.setIndeterminate(true);

    }

    public Rect getBounds(){
        return new Rect(0,0,getVideoWidth(), getVideoHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


        @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        if(mMediaPlayer != null) {
            mMediaPlayer.pause();
            mMediaPlayer = null;
       }
    }

    private TextureView.SurfaceTextureListener mSurfaceTextureListener =  new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mSurface = surface;
            mMediaPlayer.setSurfaceTexture(mSurface);
            mMediaPlayer.setData(mCurrentUri);

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            mSurface = surface;
            mMediaPlayer.setSurfaceTexture(mSurface);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            if(mSurface == surface){
                mSurface = null;
            }
            mMediaPlayer.onSurfaceTextureDestroyed(surface);
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    public boolean isPlaying() {

        if(mMediaPlayer == null){
            return false;
        }

        return mMediaPlayer.isPlaying();
    }

    public void pause() {

        if(mMediaPlayer == null){
            return;
        }

         mMediaPlayer.pause();
    }

    public void start() {
        if(mMediaPlayer == null){
            return ;
        }
        mMediaPlayer.start();
    }

    public void setData(String videoUri) {
        mLoadingProgress.setVisibility(View.VISIBLE);
        mCurrentUri = videoUri;
        initMediaPlayer();
        mMediaPlayer.setData(videoUri);

    }

    public void setRichVideoViewListener(RichVideoViewListener listener){
        mRichVideoViewListener = listener;
    }

    @Override
    public void onFirstFrameAvailable(RichMediaPlayer player) {
        mLoadingProgress.setVisibility(GONE);
        //mPlayButton.setVisibility(player.isPlaying() ? View.GONE : View.VISIBLE);
       // mPlayButton.setVisibility(View.VISIBLE);
        mFullScreenMediaController.show();
        mMainVideoContainer.setRatio(player.getVideoWidth(), player.getVideoHeight());
        adjustAspectRatio(mTextureView, player.getVideoWidth(), player.getVideoHeight());
        invalidate();
        requestLayout();
        if(mRichVideoViewListener != null){
            mRichVideoViewListener.onVideoReady(this);
        }
    }

    public int getVideoWidth(){
        return mMediaPlayer == null ? NumberUtils.INVALID : mMediaPlayer.getVideoWidth();
    }

    public int getVideoHeight(){
        return mMediaPlayer == null ? NumberUtils.INVALID : mMediaPlayer.getVideoHeight();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }


    private static void adjustAspectRatio(TextureView textureView, int videoWidth, int videoHeight) {


        int viewWidth = textureView.getWidth();
        int viewHeight = textureView.getHeight();

        double aspectRatio = (double) videoHeight / videoWidth;

        int newWidth, newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            // limited by narrow width; restrict height
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);
        } else {
            // limited by short height; restrict width
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xoff = (viewWidth - newWidth) / 2;
        int yoff = (viewHeight - newHeight) / 2;


        Matrix txform = new Matrix();
        textureView.getTransform(txform);
        txform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        txform.postTranslate(xoff, yoff);
        textureView.setTransform(txform);
    }


    @Override
    public void onCompletion(RichMediaPlayer mp) {
        onFirstFrameAvailable(mp);
    }

    @Override
    public void onVideoSizeChanged(RichMediaPlayer mp) {
        mMainVideoContainer.setRatio(mp.getVideoWidth(), mp.getVideoHeight());
        adjustAspectRatio(mTextureView, mp.getVideoWidth(), mp.getVideoHeight());
        requestLayout();
        if(mRichVideoViewListener != null){
            mRichVideoViewListener.onVideoSizeAvailable(this);
        }

    }

    public boolean videSizeKnown(){
        return mMediaPlayer == null ? false : mMediaPlayer.getVideoWidth() != NumberUtils.INVALID;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        mFullScreenMediaController.show();
        return false;
    }
}
