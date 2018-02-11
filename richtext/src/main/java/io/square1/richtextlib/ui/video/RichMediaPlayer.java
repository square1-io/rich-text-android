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

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.Size;

/**
 * Created by roberto on 12/10/15.
 */
public class RichMediaPlayer implements MediaPlayer.OnPreparedListener, android.widget.MediaController.MediaPlayerControl, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    public interface OnVideoSizeListener {
        void onVideoSizeChanged(RichMediaPlayer mp);
    }

    public interface OnCompletionListener {
        void onCompletion(RichMediaPlayer mp);
    }

    public interface FirstFrameAvailableListener {
       void onFirstFrameAvailable(RichMediaPlayer player);
    }


    public FirstFrameAvailableListener getFirstFrameAvailableListener() {
        return mFirstFrameAvailableListener;
    }

    public void setFirstFrameAvailableListener(FirstFrameAvailableListener firstFrameAvailableListener) {
        mFirstFrameAvailableListener = firstFrameAvailableListener;
    }

    public FirstFrameAvailableListener mFirstFrameAvailableListener;


    private OnCompletionListener mOnCompletionListener;

    public void setOnCompletionListener(OnCompletionListener listener) {
     mOnCompletionListener = listener;
    }

    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;


    private OnVideoSizeListener mOnVideoSizeListener;

    public void setOnVideoSizeListener(OnVideoSizeListener listener){
        mOnVideoSizeListener = listener;
    }


    private static int MEDIA_PREPARED = 1;
    private static int MEDIA_WAITING = 2;


    private static int PLAYBACK_PLAY = 1;
    private static int PLAYBACK_SHOW_FIRST_FRAME = 2;
    private static int PLAYBACK_STOP = 3;



    @Override
    public void onPrepared(MediaPlayer mp) {

        mCurrentMedia.state = MEDIA_PREPARED;
        mCurrentMedia.height = mp.getVideoHeight();
        mCurrentMedia.width = mp.getVideoWidth();
        mCurrentMedia.duration = mp.getDuration();

        if(mOnVideoSizeListener != null){
            mOnVideoSizeListener.onVideoSizeChanged(this);
        }

        // if it is not playing we want to show the first frame
        if(mCurrentMedia.playbackState != PLAYBACK_PLAY) {
            mCurrentMedia.playbackState = PLAYBACK_SHOW_FIRST_FRAME;
        }
        syncMediaState();
    }

    public void release() {

        mCurrentMedia.state = MEDIA_WAITING;
        mCurrentMedia.playbackState = PLAYBACK_STOP;
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
    }

    private static class MediaState {

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MediaState that = (MediaState) o;

            return uri.equals(that.uri);

        }

        @Override
        public int hashCode() {
            return uri.hashCode();
        }

        MediaState(Uri uri){
            this.uri = uri;
            this.currentPosition = 0;
            this.state = MEDIA_WAITING;
            this.playbackState = PLAYBACK_SHOW_FIRST_FRAME;
            this.duration = 0;
            this.width = NumberUtils.INVALID;
            this.height = NumberUtils.INVALID;
        }

        public final Size getSize(){
            Size size = null;
            if(this.width != NumberUtils.INVALID &&
                    this.height != NumberUtils.INVALID) {
                    size = new Size(width, height);
            }

            return size;
        }

        final Uri uri;
        int state;
        int playbackState;
        int width;
        int height;
        int duration;
        int currentPosition;
    }

    private Context mApplicationContext;

    public RichMediaPlayer(Context context){
        mApplicationContext = context.getApplicationContext();
        initMediaPlayer();
        mCurrentMedia = new MediaState(Uri.EMPTY);
    }

    public final Size getVideoSize(){
        return mCurrentMedia.getSize();
    }

    private void initMediaPlayer(){

        if(mMediaPlayer == null) {

            mMediaPlayer = new InternalMediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(mOnCompletionListener != null){
                        mOnCompletionListener.onCompletion(RichMediaPlayer.this);
                    }
                }
            });

            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        }
    }

    private InternalMediaPlayer mMediaPlayer;
    private SurfaceTexture mSurfaceTexture;
    private MediaState mCurrentMedia;

    public void setSurfaceTexture(SurfaceTexture texture){

        initMediaPlayer();

        if(texture != mSurfaceTexture){
            mMediaPlayer.setSurface(new Surface(texture));
            mSurfaceTexture = texture;
        }

        syncMediaState();
    }



    public void onSurfaceTextureDestroyed(SurfaceTexture texture){

        initMediaPlayer();

        if(texture == mSurfaceTexture){
            mMediaPlayer.setSurface(null);
            mSurfaceTexture = null;
        }
    }


    public boolean setData(Uri uri){

        initMediaPlayer();

        MediaState mediaState = new MediaState(uri);

        if(mediaState.equals(mCurrentMedia) == false) {

            try {
                mMediaPlayer.setDataSource(mApplicationContext, uri);
                mCurrentMedia = mediaState;
                mMediaPlayer.prepareAsync();
                return true;
            } catch (Exception exc) {
                exc.printStackTrace();
                mCurrentMedia = new MediaState(Uri.EMPTY);
                return false;
            }
        }

        return false;
    }


    public boolean setData(String uri){

        initMediaPlayer();

        if(TextUtils.isEmpty(uri)){
            return false;
        }

        MediaState mediaState = new MediaState(Uri.parse(uri));

        if(mediaState.equals(mCurrentMedia) == false) {

            try {
                mMediaPlayer.setDataSource(uri);
                mCurrentMedia = mediaState;
                mMediaPlayer.prepareAsync();
                return true;
            } catch (Exception exc) {
                exc.printStackTrace();;
                mCurrentMedia = new MediaState(Uri.EMPTY);
                return false;
            }
        }

        return false;
    }


    public void start(){
        mCurrentMedia.playbackState = PLAYBACK_PLAY;
        syncMediaState();
    }

    public void pause(){
        mCurrentMedia.playbackState = PLAYBACK_STOP;
        syncMediaState();
    }

    /**
     * Will play the media if all conditions are met
     */
     void syncMediaState(){

        if(mCurrentMedia.playbackState == PLAYBACK_SHOW_FIRST_FRAME &&
                mediaPrepared() &&
                hasSurface() &&
                isPlaying() == false){

            new ShowFrameSeekCompleteListener(0, true, this);
        }
        if(mCurrentMedia.playbackState == PLAYBACK_PLAY &&
                mediaPrepared() &&
                hasSurface() &&
                isPlaying() == false) {

            if(mCurrentMedia.currentPosition != 0 &&
                    mMediaPlayer != null){
                mMediaPlayer.start();
            }else {
                new ShowFrameSeekCompleteListener(0, false, this);
            }

        }else if(mCurrentMedia.playbackState == PLAYBACK_STOP){
            if(mMediaPlayer != null) {
                mCurrentMedia.currentPosition = mMediaPlayer.getCurrentPosition();
            }
            mMediaPlayer.pause();

        }

    }


    public void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener listener){
        if(mMediaPlayer != null) {
            mMediaPlayer.setOnBufferingUpdateListener(listener);
        }
        mOnBufferingUpdateListener = listener;
    }

    public boolean isPlaying(){

        if(mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public boolean mediaPrepared(){
        return mCurrentMedia.state == MEDIA_PREPARED;
    }

    public boolean hasSurface(){
        return mSurfaceTexture != null;
    }

    public int getVideoHeight(){
        return mCurrentMedia.height;
    }

    public int getVideoWidth(){
        return mCurrentMedia.width;
    }

    private static class ShowFrameSeekCompleteListener implements MediaPlayer.OnSeekCompleteListener {

        private RichMediaPlayer mRichMediaPlayer;
        private MediaPlayer.OnSeekCompleteListener mInitialSeekListener;
        private boolean mStopAfter;

        public ShowFrameSeekCompleteListener(int ms, boolean stopAfter, RichMediaPlayer player){
            mRichMediaPlayer = player;
            mStopAfter = stopAfter;
            mInitialSeekListener = player.mMediaPlayer.getSeekListener();
            player.mMediaPlayer.setOnSeekCompleteListener(this);
            player.mMediaPlayer.start();
            player.mMediaPlayer.seekTo(ms);
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            if(mStopAfter == true) {
                mp.pause();
            }
            //restore original seek listener
            mp.setOnSeekCompleteListener(mInitialSeekListener);
            if(mRichMediaPlayer.mFirstFrameAvailableListener != null) {
                mRichMediaPlayer.mFirstFrameAvailableListener.onFirstFrameAvailable(mRichMediaPlayer);
            }
        }
    }

    @Override
    public int getDuration() {
        return mCurrentMedia.duration;
    }

    @Override
    public int getCurrentPosition() {
        if(mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }


    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return mediaPrepared();
    }

    @Override
    public boolean canSeekBackward() {
        return mediaPrepared();
    }

    @Override
    public boolean canSeekForward() {
        return mediaPrepared();
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


}
