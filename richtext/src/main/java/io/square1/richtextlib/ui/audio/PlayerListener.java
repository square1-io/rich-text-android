/*
 * Copyright (c) 2017. Roberto  Prato <https://github.com/robertoprato>
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

import android.media.MediaPlayer;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.io.IOException;

/**
 * Created by roberto on 22/09/2017.
 */

public class PlayerListener implements Player.EventListener, ExtractorMediaSource.EventListener {

    public interface PlayerObserver {

        void onSeekComplete(ExoPlayer player);

        void onBufferingUpdate(ExoPlayer player, int progress);

        void onPlayerError(ExoPlayer player);

        void onCompletion(ExoPlayer player);

        void onPrepared(ExoPlayer player);

    }

    private PlayerObserver mObserver;
    private ExoPlayer mPlayer;

    public PlayerListener(ExoPlayer player, PlayerObserver observer) {

        mPlayer = player;
        player.addListener(this);
        mObserver = observer;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        switch (playbackState) {
            /**
             * The player does not have any media to play.
             */
            case Player.STATE_IDLE: {

            }
            break;
            /**
             * The player is not able to immediately play from its current position. This state typically
             * occurs when more data needs to be loaded.
             */
            case Player.STATE_BUFFERING: {

            }
            break;
            /**
             * The player is able to immediately play from its current position. The player will be playing if
             * {@link #getPlayWhenReady()} is true, and paused otherwise.
             */
            case Player.STATE_READY: {
                mObserver.onPrepared(mPlayer);
            }
            break;
            /**
             * The player has finished playing the media.
             */
            case Player.STATE_ENDED: {
                mObserver.onCompletion(mPlayer);
            }
        }

    }

    @Override
    public void onLoadError(IOException error) {

        try {
            mPlayer.stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        mObserver.onPlayerError(mPlayer);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

        try {
            mPlayer.stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        mObserver.onPlayerError(mPlayer);
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
