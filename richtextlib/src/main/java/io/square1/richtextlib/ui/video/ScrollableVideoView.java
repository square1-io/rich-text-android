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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.square1.richtextlib.R;


/**
 * Created by roberto on 10/10/15.
 */
public class ScrollableVideoView extends FrameLayout implements FullScreenMediaController.OnMediaControllerInteractionListener, MediaController.MediaPlayerControl, TextureView.SurfaceTextureListener {


    public static final String TAG = "SCRVIDEO";

    // all possible internal states
    private static final int STATE_ERROR              = -1;
    private static final int STATE_IDLE               = 0;
    private static final int STATE_PREPARING          = 1;
    private static final int STATE_PREPARED           = 2;
    private static final int STATE_PLAYING            = 3;
    private static final int STATE_PAUSED             = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mSeekWhenPrepared;
    private int mBufferPercentage;

    private TextureView mTextureView;
    private TextureView mFullScreenTextureView;

    private FullScreenMediaController mMediaController;

    private Uri mUri;

    private boolean mFullScreen;
    private SurfaceTexture mSurfaceTexture;
    private SurfaceTexture mFullScreenSurfaceTexture;



    private MediaPlayer mMediaPlayer = null;
    private Map<String,String> mHeaders;



    public ScrollableVideoView(Context context) {
        super(context);
        init(context);
    }

    public ScrollableVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollableVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public ScrollableVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){

        mFullScreen = false;
        mTextureView = new TextureView(context);

        //setBackgroundColor(Color.RED);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextureView.setLayoutParams(params);
        mTextureView.setSurfaceTextureListener(this);
        addView(mTextureView);

        mHeaders = new HashMap<>();
        mVideoWidth = 0;
        mVideoHeight = 0;
        mSeekWhenPrepared = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mCurrentState = STATE_IDLE;
        mTargetState  = STATE_IDLE;

    }

    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();


    }


    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();

    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, mHeaders);
    }

    public void setVideoURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        openVideo();
        requestLayout();
        invalidate();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
        mSurfaceTexture = texture;
        openVideo();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if(mMediaPlayer != null){
            //mMediaPlayer.set
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurfaceTexture = null;
        release(true);
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            mTargetState  = STATE_IDLE;
        }
    }

    private void openVideo() {

        SurfaceTexture texture = (mFullScreen) ? mFullScreenSurfaceTexture : mSurfaceTexture;

        if (mUri == null || texture == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);
        try {

            mMediaPlayer = new MediaPlayer();

            //set all the observers
            mMediaPlayer.setOnBufferingUpdateListener(mInternalOnBufferUpdateListener);
            mMediaPlayer.setOnPreparedListener(mInternalOnPreparedListener);
            mMediaPlayer.setOnCompletionListener(mInternalCompletionListener);

            if(mMediaController == null){
                mMediaController = new FullScreenMediaController(getContext());
                mMediaController.setListener(this);
                mMediaController.setAnchorView(this);
                mMediaController.setMediaPlayer(this);
            }

            Surface surface = new Surface(texture);
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setDataSource(getContext(),mUri,mHeaders);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();

            mCurrentState = STATE_PREPARING;

        }  catch (IOException ex) {
        Log.w(TAG, "Unable to open content: " + mUri, ex);
        mCurrentState = STATE_ERROR;
        mTargetState = STATE_ERROR;
       // mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        return;
    } catch (IllegalArgumentException ex) {
        Log.w(TAG, "Unable to open content: " + mUri, ex);
        mCurrentState = STATE_ERROR;
        mTargetState = STATE_ERROR;
       // mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        return;
    } finally {
      //  mPendingSubtitleTracks.clear();
    }
    }

    private void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState  = STATE_IDLE;
            }
        }
    }

    private boolean isInPlaybackState() {

        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    @Override
    public void start() {
        if (isInPlaybackState()) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }

    @Override
    public void pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        }
        mTargetState = STATE_PAUSED;
    }

    public void suspend() {
        release(false);
    }

    public void resume() {
        openVideo();
    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return mMediaPlayer.getDuration();
        }

        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(msec);
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = msec;
        }
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return mBufferPercentage;
    }


    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisibility();
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisibility();
        }
        return false;
    }

    private void toggleMediaControlsVisibility() {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            mMediaController.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK &&
                keyCode != KeyEvent.KEYCODE_VOLUME_UP &&
                keyCode != KeyEvent.KEYCODE_VOLUME_DOWN &&
                keyCode != KeyEvent.KEYCODE_VOLUME_MUTE &&
                keyCode != KeyEvent.KEYCODE_MENU &&
                keyCode != KeyEvent.KEYCODE_CALL &&
                keyCode != KeyEvent.KEYCODE_ENDCALL;
        if (isInPlaybackState() && isKeyCodeSupported && mMediaController != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    mMediaController.show();
                } else {
                    start();
                    mMediaController.hide();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!mMediaPlayer.isPlaying()) {
                    start();
                    mMediaController.hide();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                    || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    mMediaController.show();
                }
                return true;
            } else {
                toggleMediaControlsVisibility();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private MediaPlayer.OnBufferingUpdateListener mInternalOnBufferUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mBufferPercentage = percent;
        }
    };

    private MediaPlayer.OnPreparedListener mInternalOnPreparedListener = new MediaPlayer.OnPreparedListener() {



        @Override
        public void onPrepared(MediaPlayer mp) {

            mCurrentState = STATE_PREPARED;

            if (mMediaController != null) {
                mMediaController.setEnabled(true);
            }
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            setAspectRatio((double) mVideoWidth / mVideoHeight);

            if(mTextureView != null){
                adjustAspectRatio(mTextureView, mVideoWidth, mVideoHeight);
            }
            if(mFullScreenTextureView != null) {
                adjustAspectRatio(mFullScreenTextureView, mVideoWidth, mVideoHeight);
            }

            if(mTargetState == STATE_PLAYING){
                start();
            }else {
                mMediaPlayer.setOnSeekCompleteListener(mInternalFirstFrameSeekListener);
                mMediaPlayer.start();
                mMediaPlayer.seekTo(mSeekWhenPrepared);
            }
        }
    };


    public MediaPlayer.OnSeekCompleteListener mInternalFirstFrameSeekListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            mMediaPlayer.pause();
            mMediaPlayer.setOnSeekCompleteListener(null);
        }
    };

    private MediaPlayer.OnCompletionListener mInternalCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    if(mFullScreen == true){
                        mFullScreen = false;
                    }
                    mCurrentState = STATE_PLAYBACK_COMPLETED;
                    mTargetState = STATE_PLAYBACK_COMPLETED;
                    if (mMediaController != null) {
                        mMediaController.hide();
                    }
                }
            };

    @Override
    public void onRequestFullScreen() {

        final Activity activity = (Activity) getContext();
        Dialog dialog = new Dialog(activity, R.style.full_screen_dialog) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mFullScreenTextureView = new TextureView(getContext());
                mFullScreenTextureView.setSurfaceTextureListener(mFullScreenTextureListener);
                FullScreenMediaController controller = new FullScreenMediaController(getContext());
                controller.setListener(ScrollableVideoView.this);
                controller.setMediaPlayer(ScrollableVideoView.this);
                controller.setAnchorView(mFullScreenTextureView);
                setContentView(mFullScreenTextureView);

            }

            @Override
            public void show(){
                super.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                getWindow().setAttributes(lp);
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                transitionFromFullScreen();
            }
        });

        dialog.show();

//        Context context = getContext();
//
//        if(context instanceof Activity) {
//
//            mFullScreen = true;
//
//
//
//            final Activity activity = (Activity) context;
//           final WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//
//            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
//            attrs.flags ^= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            activity.getWindow().setAttributes(attrs);
//
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
//
//            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//            layoutParams.format = PixelFormat.RGBA_8888;
//            layoutParams.flags =
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                            | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
//            layoutParams.token = activity.getWindow().getDecorView().getRootView().getWindowToken();
//
//
//            //Feel free to inflate here
//           final FrameLayout container = new FrameLayout(getContext());
//            container.setBackgroundColor(Color.BLACK);
//            container.setLayoutParams(layoutParams);
//
//            DisplayMetrics metrics = new DisplayMetrics();
//            windowManager.getDefaultDisplay().getMetrics(metrics);
//            FrameLayout.LayoutParams videoLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT ,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            videoLayoutParams.gravity = Gravity.CENTER;
//            mFullScreenTextureView = new TextureView(getContext());
//            mFullScreenTextureView.setSurfaceTextureListener(mFullScreenTextureListener);
//            container.addView(mFullScreenTextureView, videoLayoutParams);
//
//           // FullScreenMediaController controller = new FullScreenMediaController(getContext());
//           // controller.setListener(this);
//           // controller.setMediaPlayer(this);
//           // controller.setAnchorView(mFullScreenTextureView);
//
//            //Must wire up back button, otherwise it's not sent to our activity
//            container.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        mFullScreen = false;
//                        mMediaController.setAnchorView(ScrollableVideoView.this);
//                        windowManager.removeView(container);
//                    }
//                    return true;
//                }
//            });
//
//
//            mFullscreenContainer = container;
//            requestLayout();
//            invalidate();
//        }

    }

    private void transitionToFullScreen(){

        mFullScreen = true;

        if(mFullScreenSurfaceTexture != null) {
            mMediaPlayer.setSurface(new Surface(mFullScreenSurfaceTexture));
        }

    }

    private void transitionFromFullScreen(){

        mFullScreen = false;

        if(mSurfaceTexture != null) {
            mMediaPlayer.setSurface(new Surface(mSurfaceTexture));
        }

    }


    private TextureView.SurfaceTextureListener mFullScreenTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mFullScreenSurfaceTexture = surface;
            transitionToFullScreen();
            adjustAspectRatio(mFullScreenTextureView,mVideoWidth,mVideoHeight);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            mFullScreenSurfaceTexture = null;
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };


    private double mTargetAspect = -1.0;

    /**
     * Sets the desired aspect ratio.  The value is <code>width / height</code>.
     */
    public void setAspectRatio(double aspectRatio) {
        if (aspectRatio < 0) {
            throw new IllegalArgumentException();
        }
        Log.d(TAG, "Setting aspect ratio to " + aspectRatio + " (was " + mTargetAspect + ")");
        if (mTargetAspect != aspectRatio) {
            mTargetAspect = aspectRatio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure target=" + mTargetAspect +
                " width=[" + MeasureSpec.toString(widthMeasureSpec) +
                "] height=[" + View.MeasureSpec.toString(heightMeasureSpec) + "]");

        // Target aspect ratio will be < 0 if it hasn't been set yet.  In that case,
        // we just use whatever we've been handed.
        if (mTargetAspect > 0) {
            int initialWidth = MeasureSpec.getSize(widthMeasureSpec);
            int initialHeight = MeasureSpec.getSize(heightMeasureSpec);

            // factor the padding out
            int horizPadding = getPaddingLeft() + getPaddingRight();
            int vertPadding = getPaddingTop() + getPaddingBottom();
            initialWidth -= horizPadding;
            initialHeight -= vertPadding;

            double viewAspectRatio = (double) initialWidth / initialHeight;
            double aspectDiff = mTargetAspect / viewAspectRatio - 1;

            if (Math.abs(aspectDiff) < 0.01) {
                // We're very close already.  We don't want to risk switching from e.g. non-scaled
                // 1280x720 to scaled 1280x719 because of some floating-point round-off error,
                // so if we're really close just leave it alone.
                Log.d(TAG, "aspect ratio is good (target=" + mTargetAspect +
                        ", view=" + initialWidth + "x" + initialHeight + ")");
            } else {
                if (aspectDiff > 0) {
                    // limited by narrow width; restrict height
                    initialHeight = (int) (initialWidth / mTargetAspect);
                } else {
                    // limited by short height; restrict width
                    initialWidth = (int) (initialHeight * mTargetAspect);
                }
                Log.d(TAG, "new size=" + initialWidth + "x" + initialHeight + " + padding " +
                        horizPadding + "x" + vertPadding);
                initialWidth += horizPadding;
                initialHeight += vertPadding;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, MeasureSpec.EXACTLY);
            }
        }

        //Log.d(TAG, "set width=[" + MeasureSpec.toString(widthMeasureSpec) +
        //        "] height=[" + View.MeasureSpec.toString(heightMeasureSpec) + "]");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        Log.v(TAG, "video=" + videoWidth + "x" + videoHeight +
                " view=" + viewWidth + "x" + viewHeight +
                " newView=" + newWidth + "x" + newHeight +
                " off=" + xoff + "," + yoff);

        Matrix txform = new Matrix();
        textureView.getTransform(txform);
        txform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        txform.postTranslate(xoff, yoff);
        textureView.setTransform(txform);
    }

}
