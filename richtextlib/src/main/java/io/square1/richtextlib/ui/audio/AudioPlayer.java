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

package io.square1.richtextlib.ui.audio;

import android.app.Activity;
import android.content.Context;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.square1.richtextlib.R;

/**
 * Created by roberto on 17/09/15.
 */
public class AudioPlayer implements AudioPlayerHolder.AudioPlayerProvider , MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {


    private static HashMap<String,Media> mMediaMap;

    private HashMap<AudioPlayerHolder,String> mAudioToPlayer;


    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private String mCurrentFile;
    private String mPendingFile;
    private Handler mHandler;

    public AudioPlayer(Context activity){
        mContext = activity.getApplicationContext();
        mAudioToPlayer = new HashMap<>();
        mMediaMap = new HashMap<>();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0) {
                    synchronizeHolders();
                }else if (msg.what == 1){
                    synchronizeCurrent();
                }
            }
        };

    }

    private Media getMedia(String file){
        Media media = mMediaMap.get(file);
        if(media == null){
            media = new Media();
            media.file = file;
            mMediaMap.put(file,media);
        }

        return media;
    }

    private void synchronizeHolders(){

        Set<AudioPlayerHolder> keys = mAudioToPlayer.keySet();
        for(AudioPlayerHolder holder : keys){
            holder.synchronizeState();
        }
    }

    private void synchronizeCurrent() {

        Set<Map.Entry<AudioPlayerHolder,String>> entries = mAudioToPlayer.entrySet();
        for(Map.Entry<AudioPlayerHolder,String> entry : entries){
            if(TextUtils.equals(entry.getValue(),mCurrentFile)){
                entry.getKey().synchronizeState();
                mHandler.sendEmptyMessageDelayed(1,1000);
                return;
            }
        }
    }

    public void onCreate(){

    }

    public void onResume(){

    }

    public void onPause(){
        onStop(mCurrentFile);
    }

    public void onDestroy(){
        onStop(mCurrentFile);
        cleanCurrentPLayer();
        Set<AudioPlayerHolder> keys = mAudioToPlayer.keySet();
        for(AudioPlayerHolder holder : keys){
            holder.destroy();
        }

        mAudioToPlayer.clear();
    }

    @Override
    public void registerHolder(String audio, AudioPlayerHolder holder) {
        mAudioToPlayer.put(holder, audio);
    }

    @Override
    public void deregisterHolder(AudioPlayerHolder holder) {
        mAudioToPlayer.remove(holder);
    }

    @Override
    public void onPlay(String audio) {

        if(TextUtils.equals(mCurrentFile, audio) == false &&
                TextUtils.equals(mPendingFile, audio) == false ){

            replaceCurrentPLayer();

            //prepare

            MediaPlayer mediaPlayer = new MediaPlayer();

            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);

            try {
                mPendingFile = audio;
                mediaPlayer.setDataSource(audio);
                mediaPlayer.prepareAsync();
            }catch (Exception exc){
                if(mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mPendingFile = null;
            }
        }else if(mMediaPlayer != null &&
                mMediaPlayer.isPlaying() == false ){
            mMediaPlayer.start();
            mHandler.sendEmptyMessage(1);
            notifyState();
        }

    }

    @Override
    public void onStop(String audio) {

        if(TextUtils.isEmpty(audio)){
            return;
        }

        if(TextUtils.equals(mCurrentFile, audio) == true ){
            mMediaPlayer.pause();
            mHandler.removeMessages(0);
            mHandler.removeMessages(1);
            notifyState();
        }
        else if(TextUtils.equals(mPendingFile, audio) == true ){
            mPendingFile = null;
            cleanCurrentPLayer();
        }

    }

    @Override
    public void onRew(String audio) {
        seek(audio,false);
    }

    @Override
    public void onFwd(String audio) {
        seek(audio,true);
    }

    @Override
    public int getDuration(String audio) {
        return getMedia(audio).duration;
    }

    @Override
    public int getProgress(String audio) {
        if(mMediaPlayer != null &&
                TextUtils.equals(mCurrentFile,audio)) {
           return mMediaPlayer.getCurrentPosition() ;
        }

        return 0;
    }

    @Override
    public boolean isPlaying(String audio) {
        if( mMediaPlayer != null &&
                TextUtils.equals(audio, mCurrentFile) ){
            return mMediaPlayer.isPlaying();
        }
        if(TextUtils.equals(mPendingFile,audio) == true){
            return true;
        }

        return false;
    }

    @Override
    public String getLabelForProgress(int progress, String audio) {
        return AudioPlayerHolder.formatTime(progress);
    }



    public void replaceCurrentPLayer(){

        if(mMediaPlayer != null){

            if(TextUtils.isEmpty(mCurrentFile) == false){
                notifyState();
            }

            mHandler.removeMessages(0);
            mHandler.removeMessages(1);

            mMediaPlayer.release();
            mCurrentFile = null;
            mMediaPlayer = null;
        }
    }

    public void cleanCurrentPLayer(){

        if(mMediaPlayer != null){

            if(TextUtils.isEmpty(mCurrentFile) == false){
                notifyState();
            }

            mHandler.removeMessages(0);
            mHandler.removeMessages(1);

            mMediaPlayer.release();
            mCurrentFile = null;
            mMediaPlayer = null;
        }
    }


    private void seek(String file , boolean forward){

        if(TextUtils.equals(mCurrentFile, file) &&
                mMediaPlayer != null ){

            int currentPosition = mMediaPlayer.getCurrentPosition();
            Media media = getMedia(file);
            if(media.duration == Media.DURATION_UNKNOWN) {
                int duration = mMediaPlayer.getDuration();
                media.duration = duration;
            }
            final int delta = 2 * 1000;
            if(forward) {
                currentPosition = Math.min(media.duration, currentPosition + delta);
            }else{
                currentPosition = Math.max(0, currentPosition - delta);
            }
            mMediaPlayer.seekTo(currentPosition);

        }
    }



    private void notifyState(){
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer = mp;
        //ready to play
        mCurrentFile = mPendingFile;
        mPendingFile = null;
        Media media = getMedia(mCurrentFile);
        if(media.duration == Media.DURATION_UNKNOWN) {
            int duration = mMediaPlayer.getDuration();
            media.duration = duration;
        }
        mMediaPlayer.start();
        notifyState();
        mHandler.sendEmptyMessageDelayed(1,1000);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mp == mMediaPlayer){
            //ready to play
            notifyState();
            cleanCurrentPLayer();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(mContext,
                R.string.audio_media_player,
                Toast.LENGTH_LONG).show();

        return false;
    }
}
