package io.square1.richtextlib.ui.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.AspectRatioFrameLayout;

/**
 * Created by roberto on 12/10/15.
 */
public class RichVideoView extends FrameLayout implements RichMediaPlayer.FirstFrameAvailableListener ,
        MediaPlayer.OnBufferingUpdateListener,
        RichMediaPlayer.OnCompletionListener {


    private static final FrameLayout.LayoutParams MATCH_PARENT_PARAMS =
            new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    static {
        MATCH_PARENT_PARAMS.gravity = Gravity.CENTER;
    }



    private AspectRatioFrameLayout mMainVideoContainer;
    private TextureView mTextureView;
    private RichMediaPlayer mMediaPlayer;

    private ImageView mPlayButton;
    private ProgressBar mLoadingProgress;

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

    public void init(){

        setBackgroundColor(Color.BLUE);

        mMediaPlayer = new RichMediaPlayer(getContext());
        mMediaPlayer.setFirstFrameAvailableListener(this);
        mMediaPlayer.setOnCompletionListener(this);

        //setup inline containers
        mMainVideoContainer = new AspectRatioFrameLayout(getContext());
        mMainVideoContainer.setLayoutParams(MATCH_PARENT_PARAMS);
        mMainVideoContainer.setBackgroundColor(Color.RED);
        addView(mMainVideoContainer);
        //add Texture View
        mTextureView = new TextureView(getContext());
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        mTextureView.setLayoutParams(MATCH_PARENT_PARAMS);
        mMainVideoContainer.addView(mTextureView);

        //setup the progress bar
        mLoadingProgress = new ProgressBar(getContext(),null,android.R.attr.progressBarStyleSmall);
        mLoadingProgress.setIndeterminate(true);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mLoadingProgress.setLayoutParams(params);
        addView(mLoadingProgress);

        mPlayButton = new ImageView(getContext());
        mPlayButton.setLayoutParams(MATCH_PARENT_PARAMS);
        mPlayButton.setScaleType(ImageView.ScaleType.CENTER);
        mPlayButton.setImageResource(R.drawable.fa_play_circle);
        mPlayButton.setVisibility(GONE);

        mPlayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
            }
        });

        addView(mPlayButton);

    }




    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        mMediaPlayer.release();
    }

    private TextureView.SurfaceTextureListener mSurfaceTextureListener =  new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mMediaPlayer.setSurfaceTexture(surface);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            mMediaPlayer.onSurfaceTextureDestroyed(surface);
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void pause() {
         mMediaPlayer.pause();
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void setData(Uri videoUri) {
        mLoadingProgress.setVisibility(View.VISIBLE);
        mPlayButton.setVisibility(View.GONE);
        mMediaPlayer.setData(videoUri);

    }

    @Override
    public void onFirstFrameAvailable(RichMediaPlayer player) {
        mLoadingProgress.setVisibility(GONE);
        mPlayButton.setVisibility(player.isPlaying() ? View.GONE : View.VISIBLE);
        mMainVideoContainer.setRatio(player.getVideoWidth(), player.getVideoHeight());
        adjustAspectRatio(mTextureView, player.getVideoWidth(), player.getVideoHeight());
        invalidate();
        requestLayout();
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
}
