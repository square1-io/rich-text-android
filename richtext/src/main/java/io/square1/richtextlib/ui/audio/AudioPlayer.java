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

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.square1.richtextlib.R;

/**
 * Created by roberto on 17/09/15.
 */
public class AudioPlayer implements AudioPlayerHolder.AudioPlayerProvider,
        PlayerListener.PlayerObserver {

    public static int DEFAULT_TIME_OUT = 60000;

    protected static HashMap<String, Media> mMediaMap;

    protected HashMap<AudioPlayerHolder, String> mAudioToPlayer;

    protected PlayerListener mPlayerListener;
    protected ExoPlayer mMediaPlayer;
    protected Context mContext;
    protected String mCurrentFile;
    protected String mPendingFile;
    protected Handler mHandler;

    protected String mAppName;

    public AudioPlayer(Context activity) {

        mContext = activity.getApplicationContext();
        mAppName = getApplicationName();
        mAudioToPlayer = new HashMap<>();
        mMediaMap = new HashMap<>();
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 0) {
                    synchronizeHolders();
                }
                else if (msg.what == 1) {
                    synchronizeCurrent();
                }
            }
        };

    }

    protected Media getMedia(String file) {

        Media media = mMediaMap.get(file);
        if (media == null) {
            media = new Media();
            media.file = file;
            mMediaMap.put(file, media);
        }

        return media;
    }

    protected void synchronizeHolders() {

        Set<AudioPlayerHolder> keys = mAudioToPlayer.keySet();
        for (AudioPlayerHolder holder : keys) {
            holder.synchronizeState();
        }
    }

    protected void synchronizeCurrent() {

        Set<Map.Entry<AudioPlayerHolder, String>> entries = mAudioToPlayer.entrySet();
        for (Map.Entry<AudioPlayerHolder, String> entry : entries) {
            if (TextUtils.equals(entry.getValue(), mCurrentFile)) {
                entry.getKey().synchronizeState();
            }
        }
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    public void onCreate() {

    }

    public void onResume() {

    }

    public void onPause() {

        onStop(mCurrentFile);
    }

    public void onDestroy() {

        onStop(mCurrentFile);
        cleanCurrentPLayer();
        Set<AudioPlayerHolder> keys = mAudioToPlayer.keySet();
        for (AudioPlayerHolder holder : keys) {
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

        if (TextUtils.equals(mCurrentFile, audio) == false &&
                TextUtils.equals(mPendingFile, audio) == false) {

            replaceCurrentPLayer();

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            TrackSelection.Factory audioTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(audioTrackSelectionFactory);

            SimpleExoPlayer mediaPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioManager.STREAM_MUSIC).build());

            mediaPlayer.setPlayWhenReady(true);
            mPlayerListener = new PlayerListener(mediaPlayer, this);
            mediaPlayer.addListener(mPlayerListener);

            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            DefaultHttpDataSourceFactory dataSource = new DefaultHttpDataSourceFactory(mAppName,
                    null, DEFAULT_TIME_OUT, DEFAULT_TIME_OUT, true);

            try {

                mPendingFile = audio;
                Handler handler = new Handler(Looper.getMainLooper());
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(audio),
                        dataSource, extractorsFactory, handler, mPlayerListener);
                // Prepare the player with the source.
                mediaPlayer.prepare(mediaSource);
                mMediaPlayer = mediaPlayer;
            }
            catch (Exception exc) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mPendingFile = null;
            }
        }
        else if (mMediaPlayer != null &&
                mMediaPlayer.getPlayWhenReady() == false) {
            mMediaPlayer.setPlayWhenReady(true);
            mHandler.sendEmptyMessage(1);
            notifyState();
        }

    }

    @Override
    public void onStop(String audio) {

        if (TextUtils.isEmpty(audio)) {
            return;
        }

        if (TextUtils.equals(mCurrentFile, audio) == true) {
            mMediaPlayer.setPlayWhenReady(false);
            mHandler.removeMessages(0);
            mHandler.removeMessages(1);
            notifyState();
        }
        else if (TextUtils.equals(mPendingFile, audio) == true) {
            mPendingFile = null;
            cleanCurrentPLayer();
        }

    }

    @Override
    public void onRew(String audio) {

        seek(audio, false);
    }

    @Override
    public void onFwd(String audio) {

        seek(audio, true);
    }

    @Override
    public int getDuration(String audio) {

        return (int) getMedia(audio).duration;
    }

    @Override
    public int getProgress(String audio) {

        if (mMediaPlayer != null &&
                TextUtils.equals(mCurrentFile, audio)) {
            return (int) mMediaPlayer.getCurrentPosition();
        }

        return 0;
    }

    @Override
    public boolean isPlaying(String audio) {

        if (mMediaPlayer != null &&
                TextUtils.equals(audio, mCurrentFile)) {
            return mMediaPlayer.getPlayWhenReady();
        }
        if (TextUtils.equals(mPendingFile, audio) == true) {
            return true;
        }

        return false;
    }

    @Override
    public String getLabelForProgress(int progress, String audio) {

        return AudioPlayerHolder.formatTime(progress);
    }

    public void replaceCurrentPLayer() {

        if (mMediaPlayer != null) {

            if (TextUtils.isEmpty(mCurrentFile) == false) {
                notifyState();
            }

            mHandler.removeMessages(0);
            mHandler.removeMessages(1);

            mMediaPlayer.release();
            mCurrentFile = null;
            mMediaPlayer = null;
        }
    }

    public void cleanCurrentPLayer() {

        if (mMediaPlayer != null) {

            if (TextUtils.isEmpty(mCurrentFile) == false) {
                notifyState();
            }

            mHandler.removeMessages(0);
            mHandler.removeMessages(1);

            mMediaPlayer.release();
            mCurrentFile = null;
            mMediaPlayer = null;
        }
    }

    protected void seek(String file, boolean forward) {

        if (TextUtils.equals(mCurrentFile, file) &&
                mMediaPlayer != null) {

            long currentPosition = mMediaPlayer.getCurrentPosition();
            Media media = getMedia(file);
            if (media.duration == Media.DURATION_UNKNOWN) {
                long duration = mMediaPlayer.getDuration();
                media.duration = duration;
            }
            final int delta = 2 * 1000;
            if (forward) {
                currentPosition = Math.min(media.duration, currentPosition + delta);
            }
            else {
                currentPosition = Math.max(0, currentPosition - delta);
            }
            mMediaPlayer.seekTo(currentPosition);

        }

    }

    @Override
    public void seek(String file, long position) {

        if (TextUtils.equals(mCurrentFile, file) &&
                mMediaPlayer != null) {

            Media media = getMedia(file);
            if (media.duration == Media.DURATION_UNKNOWN) {
                long duration = mMediaPlayer.getDuration();
                media.duration = duration;
            }

            mMediaPlayer.seekTo(position);

        }
    }

    protected void notifyState() {

        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onSeekComplete(ExoPlayer player) {

    }

    @Override
    public void onBufferingUpdate(ExoPlayer player, int progress) {

    }

    @Override
    public void onCompletion(ExoPlayer player) {

        if (player == mMediaPlayer) {
            //ready to play
            notifyState();
            cleanCurrentPLayer();
        }

    }

    @Override
    public void onPrepared(ExoPlayer player) {

        if (player.getPlayWhenReady() == true &&
                mPendingFile != null) {// if this is null we are already playing a file

            mMediaPlayer = player;
            //ready to play
            mCurrentFile = mPendingFile;
            mPendingFile = null;
            Media media = getMedia(mCurrentFile);
            if (media.duration == Media.DURATION_UNKNOWN) {
                long duration = mMediaPlayer.getDuration();
                media.duration = duration;
            }
        }
        notifyState();
        mHandler.sendEmptyMessageDelayed(1, 1000);

    }

    @Override
    public void onPlayerError(ExoPlayer player) {

        mMediaPlayer = player;

        try {
            cleanCurrentPLayer();
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        //Toast.makeText(mContext,R.string.audio_media_player,Toast.LENGTH_SHORT).show();

        Set<Map.Entry<AudioPlayerHolder, String>> entries = mAudioToPlayer.entrySet();
        for (Map.Entry<AudioPlayerHolder, String> entry : entries) {
            if (TextUtils.equals(entry.getValue(), mPendingFile)) {
                entry.getKey().showAudioNotAvailable();
            }
        }

    }

    protected String getApplicationName() {

        final PackageManager pm = mContext.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(mContext.getPackageName(), 0);
            final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
            return applicationName;
        }
        catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }

        return "(unknown)";
    }

}
