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


import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


import io.square1.richtextlib.R;

/**
* Created by roberto on 05/03/15.
*/
public class AudioPlayerHolder {

    public static String formatTime(int time){

        int minutes = time > 0 ? time / (60 * 1000) : 0;
        int seconds = time > 0 ? (time / 1000) % 60 : 0;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public interface AudioPlayerProvider {

        void registerHolder(String audio, AudioPlayerHolder holder);
        void deregisterHolder(AudioPlayerHolder holder);

        void onPlay(String audio);
        void onStop(String audio);
        void onRew(String audio);
        void onFwd(String audio);

        int getDuration(String audio);
        int getProgress(String audio);
        boolean isPlaying(String audio);

        String getLabelForProgress(int progress, String audio);

        //follows Activity Lifecycle
         void onCreate();
         void onResume();
         void onPause();
         void onDestroy();


    }


    private View mRewButton;
    private View mPauseButton;
    private View mPlayButton;
    private View mFfwdButton;
    private TextView mTimeLabel;
    private TextView mTimeCurrentLabel;
    private SeekBar mProgress;

    private String mCurrentFile;

    private AudioPlayerProvider mAudioPlayerProvider;

    private View.OnClickListener mInternalClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(R.id.play == view.getId()){
                mAudioPlayerProvider.onPlay(mCurrentFile);
                if(ZERO_TIME.equalsIgnoreCase(mTimeLabel.getText().toString())) {
                    mTimeLabel.setText(R.string.loading);
                }
            }
            else if(R.id.pause == view.getId()){
                mAudioPlayerProvider.onStop(mCurrentFile);
            }
            else if(R.id.rew == view.getId()){
                mAudioPlayerProvider.onRew(mCurrentFile);
            }
            else if(R.id.ffwd == view.getId()){
                mAudioPlayerProvider.onFwd(mCurrentFile);
            }
        }
    };


    final String ZERO_TIME ;

    public AudioPlayerHolder(View v, AudioPlayerProvider provider) {

        mAudioPlayerProvider = provider;

        ZERO_TIME = v.getContext().getString(R.string.zero_time);


        mProgress = (SeekBar)v.findViewById(R.id.mediacontroller_progress);
        mTimeLabel = (TextView)v.findViewById(R.id.time);
        mTimeCurrentLabel = (TextView)v.findViewById(R.id.time_current);

        (mRewButton = v.findViewById(R.id.rew) ).setOnClickListener(mInternalClickListener);
        (mPauseButton = v.findViewById(R.id.pause)).setOnClickListener(mInternalClickListener);
        (mPlayButton = v.findViewById(R.id.play)).setOnClickListener(mInternalClickListener);
        (mFfwdButton = v.findViewById(R.id.ffwd)).setOnClickListener(mInternalClickListener);
    }

    protected void synchronizeState(){


        int duration = mAudioPlayerProvider.getDuration(mCurrentFile);
        //is duration available ?
        if(duration > 0) {
            mTimeLabel.setText(mAudioPlayerProvider.getLabelForProgress(duration, mCurrentFile));
            mProgress.setMax(duration);
        }

        int progress = mAudioPlayerProvider.getProgress(mCurrentFile);
        //is progress available ?
        if(progress >= 0){
            mTimeCurrentLabel.setText(mAudioPlayerProvider.getLabelForProgress(progress, mCurrentFile));
            mProgress.setProgress(progress);
        }


        if(mAudioPlayerProvider.isPlaying(mCurrentFile) == false){
            mPlayButton.setVisibility(View.VISIBLE);
            mPauseButton.setVisibility(View.GONE);

        }else{
            mPlayButton.setVisibility(View.GONE);
            mPauseButton.setVisibility(View.VISIBLE);
        }
//
//        else if(PPlusNavigation.PlayStateRequest.EStop == request){
//            mPlayButton.setVisibility(View.VISIBLE);
//            mPauseButton.setVisibility(View.GONE);
//        }else if(PPlusNavigation.PlayStateRequest.EStopReplaced == request){
//
//            mTimeLabel.setText(ZERO_TIME);
//            mTimeCurrentLabel.setText(ZERO_TIME);
//            mProgress.setProgress(0);
//
//            mPlayButton.setVisibility(View.VISIBLE);
//            mPauseButton.setVisibility(View.GONE);
//        }


    }


    public void setAudioFile(String audioFileId) {

       if(TextUtils.equals(mCurrentFile,audioFileId) == false){
           mAudioPlayerProvider.registerHolder(audioFileId,this);
           mCurrentFile = audioFileId;
           mTimeLabel.setText(ZERO_TIME);
           mTimeCurrentLabel.setText(ZERO_TIME);
           mProgress.setProgress(0);
           synchronizeState();

       }

    }

    public void destroy(){
        mAudioPlayerProvider.onStop(mCurrentFile);
        mAudioPlayerProvider = null;
    }

}
