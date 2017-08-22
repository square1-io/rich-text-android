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
import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import io.square1.richtextlib.R;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.Size;

/**
 * Created by roberto on 12/10/15.
 */
public class RichVideoView extends FrameLayout implements RichMediaPlayer.FirstFrameAvailableListener ,
        MediaPlayer.OnBufferingUpdateListener,
        RichMediaPlayer.OnCompletionListener,
        RichMediaPlayer.OnVideoSizeListener {


    public void setSurface(SurfaceTexture surface) {
        mMediaPlayer.setSurfaceTexture(surface);
    }

    public RichMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public interface  RichVideoViewListener {

         void onVideoReady(RichVideoView videoView);
         void onVideoSizeAvailable(RichVideoView videoView);

    }



    private RichVideoViewListener mRichVideoViewListener;

    private VideoControls mControlsContainer;

    private FrameLayout mMainVideoContainer;
    private TextureView mTextureView;
    private RichMediaPlayer mMediaPlayer;

    private SurfaceTexture mSurface;
    private int mSurfaceWidth;
    private int mSurfaceHeight;


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
        }

        mMediaPlayer.setFirstFrameAvailableListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnVideoSizeListener(this);

        if(mSurface != null){
            mMediaPlayer.setSurfaceTexture(mSurface);
        }

    }
    public void init(){

        initMediaPlayer();

        LayoutInflater.from(getContext())
                .inflate(R.layout.internal_richtext_video_display,
                this, true);

        mMainVideoContainer = (FrameLayout)findViewById(R.id.internal_aspect_ratio_view);
        mTextureView =  (TextureView)findViewById(R.id.internal_texture_view);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);

        if(mTextureView.isAvailable() == true){
            mSurfaceTextureListener.onSurfaceTextureAvailable(mTextureView.getSurfaceTexture(),
                    mTextureView.getWidth()
                    ,mTextureView.getHeight());
        }

        mControlsContainer = new VideoControls(getContext(), this);


       ViewGroup.LayoutParams currentLayoutParams = getLayoutParams();

        if(currentLayoutParams == null){
            currentLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        currentLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        setLayoutParams(currentLayoutParams);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        //setup the progress bar
        mLoadingProgress = (ProgressBar) findViewById(R.id.internal_progress);
        mLoadingProgress.setIndeterminate(true);

    }

    public void handover(RichVideoView destination){

        destination.mMediaPlayer = mMediaPlayer;

        if(destination.mTextureView.isAvailable() == true){

            destination.mSurfaceTextureListener.onSurfaceTextureAvailable(destination.mTextureView.getSurfaceTexture(),
                    destination.mTextureView.getWidth(),
                    destination.mTextureView.getHeight());
        }

        destination.initMediaPlayer();
        destination.mMediaPlayer.syncMediaState();



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
//        if(mMediaPlayer != null) {
//            mMediaPlayer.pause();
//            mMediaPlayer = null;
//       }
    }

    private TextureView.SurfaceTextureListener mSurfaceTextureListener =  new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mSurface = surface;
            mSurfaceWidth = width;
            mSurfaceHeight = height;
            mMediaPlayer.setSurfaceTexture(mSurface);
            mMediaPlayer.setData(mCurrentUri);

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            mSurface = surface;
            mSurfaceWidth = width;
            mSurfaceHeight = height;
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

        if(mMediaPlayer == null ||
                mMediaPlayer.isPlaying()){
            return ;
        }

        mMediaPlayer.start();
    }

    public void setData(String videoUri) {
        if(TextUtils.equals(videoUri,mCurrentUri) == false) {
            mLoadingProgress.setVisibility(View.VISIBLE);
            mCurrentUri = videoUri;
            initMediaPlayer();
            mMediaPlayer.setData(videoUri);
        }
    }

    public void setRichVideoViewListener(RichVideoViewListener listener){
        mRichVideoViewListener = listener;
    }

    @Override
    public void onFirstFrameAvailable(RichMediaPlayer player) {

        mLoadingProgress.setVisibility(GONE);

        if(mControlsContainer != null) {
            mControlsContainer.updateControls();
            mControlsContainer.showControls();
            mControlsContainer.setFullScreenButtonVisible(true);
        }

        adjustAspectRatio( this,
                mMainVideoContainer,
                player.getVideoWidth(),
                player.getVideoHeight());

        adjustAspectRatio(mTextureView,
                player.getVideoWidth(),
                player.getVideoHeight());

        invalidate();
        requestLayout();
        if(mRichVideoViewListener != null){
            mRichVideoViewListener.onVideoReady(this);
        }
    }


    public Size getVideoSize(){
        if(mMediaPlayer != null){
           return mMediaPlayer.getVideoSize();
        }
        return null;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }


    public static void adjustAspectRatio(View parentView , FrameLayout layout, double videoWidth, double videoHeight) {

       double width  = layout.getMeasuredWidth();
        double newHeight = videoHeight / videoWidth * width;

        FrameLayout.LayoutParams newParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int)newHeight ,
                Gravity.CENTER);

        layout.setLayoutParams(newParams);

        parentView.requestLayout();
    }
    public static void adjustAspectRatio(TextureView textureView, int videoWidth, int videoHeight) {


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
        if(mControlsContainer != null) {
            mControlsContainer.updateControls();
        }
    }

    @Override
    public void onVideoSizeChanged(RichMediaPlayer mp) {

        adjustAspectRatio(this, mMainVideoContainer, mp.getVideoWidth(), mp.getVideoHeight());
        adjustAspectRatio(mTextureView, mp.getVideoWidth(), mp.getVideoHeight());
        requestLayout();
        if(mRichVideoViewListener != null){
            mRichVideoViewListener.onVideoSizeAvailable(this);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mControlsContainer != null) {
            mControlsContainer.showControls();
        }
        return super.onTouchEvent(event);
    }

    public void release(){
        try{
        if(mMediaPlayer != null){
            mMediaPlayer.release();
        }
        }catch (Exception e){}
    }


    public boolean toggleFullScreen(boolean fullscreen) {

        final Context context = getContext();

        if(!(context instanceof Activity)){
            return false;
        }

        Activity activity = (Activity)context;

        // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
        if (fullscreen) {
            FullScreenVideoFragment fullScreenVideoFragment = new FullScreenVideoFragment();
            fullScreenVideoFragment.presentVideoFullScreen(activity , this);
        }else {
            //mSurface
            mSurfaceTextureListener.onSurfaceTextureAvailable(mSurface, mSurfaceWidth, mSurfaceHeight);
            if(mControlsContainer != null) {
                mControlsContainer.updateControls();
            }
        }

        return true;
    }
}
