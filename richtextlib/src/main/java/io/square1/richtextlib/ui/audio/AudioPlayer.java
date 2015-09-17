package io.square1.richtextlib.ui.audio;

import android.app.Activity;
import android.content.Context;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by roberto on 17/09/15.
 */
public class AudioPlayer implements AudioPlayerHolder.AudioPlayerProvider , MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {


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
                synchronizeHolders();
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
            holder.reset();
        }
    }

    @Override
    public void registerHolder(String audio, AudioPlayerHolder holder) {
        mAudioToPlayer.put(holder,audio);
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
            Bundle status = new Bundle();
            //notifyState(status);
            mHandler.sendEmptyMessageDelayed(0,1000);
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
            Bundle status = new Bundle();
            notifyState(status);
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
        return 0;
    }

    @Override
    public boolean isPlaying(String audio) {
        return false;
    }

    @Override
    public String getLabelForProgress(int progress, String audio) {
        return null;
    }



    public void replaceCurrentPLayer(){

        if(mMediaPlayer != null){

            if(TextUtils.isEmpty(mCurrentFile) == false){
                Bundle status = new Bundle();
                notifyState(status);
            }

            mHandler.removeMessages(0);

            mMediaPlayer.release();
            mCurrentFile = null;
            mMediaPlayer = null;
        }
    }

    public void cleanCurrentPLayer(){

        if(mMediaPlayer != null){

            if(TextUtils.isEmpty(mCurrentFile) == false){
                Bundle status = new Bundle();

                notifyState(status);
            }

            mHandler.removeMessages(0);

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



    private void notifyState(Bundle data){


    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer = mp;
        //if(mp == mMediaPlayer){
        //ready to play
        mCurrentFile = mPendingFile;
        mPendingFile = null;
        Bundle status = new Bundle();
        mMediaPlayer.start();
        notifyState(status);
        mHandler.sendEmptyMessageDelayed(0,1000);
        //  }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mp == mMediaPlayer){
            //ready to play
            Bundle status = new Bundle();
            notifyState(status);
            cleanCurrentPLayer();
        }
    }

}
